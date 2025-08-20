package com.dt.order_service.service;

import com.dt.order_service.dto.*;
import com.dt.order_service.exception.InsufficientStockException;
import com.dt.order_service.exception.ResourceNotFoundException;
import com.dt.order_service.model.Order;
import com.dt.order_service.model.OrderItem;
import com.dt.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    @Value("${services.user.url}")
    String userServiceUrl;
    @Value("${services.product.url}")
    String productServiceUrl;
    @Value("${services.payment.url}")
    String paymentServiceUrl;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(String userEmail, OrderRequest orderRequest) {

        UserResponse user = webClientBuilder.build().get()
                .uri(userServiceUrl + "/api/v1/users/details/{email}", userEmail)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();

        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + userEmail);
        }

        List<StockCheckRequest> stockCheckItems = orderRequest.items().stream()
                .map(item -> new StockCheckRequest(item.productId(), item.quantity()))
                .toList();

        List<StockCheckResponse> stockCheckResults = webClientBuilder.build().post()
                .uri(productServiceUrl + "/api/v1/products/stock-check")
                .bodyValue(stockCheckItems)
                .retrieve()
                .bodyToFlux(StockCheckResponse.class)
                .collectList()
                .block();

        boolean isInStock = stockCheckResults.stream().allMatch(StockCheckResponse::inStock);

        if (!isInStock) {
            throw new InsufficientStockException("One or more items are out of stock.");
        }

        // Create and save the order with a "PENDING" status
        Order order = createAndSaveInitialOrder(user.id(), stockCheckResults, orderRequest);

        // Process payment
        boolean paymentSuccess = false;

        try {
            // Try to process the payment
            paymentSuccess = processPayment(order);
        } catch (Exception e) {
            log.error("Payment service call failed for order {}: {}", order.getId(), e.getMessage());
        }

        if (paymentSuccess) {
            // Commit the stock decrease by calling the product service again
            webClientBuilder.build().post()
                    .uri(productServiceUrl + "api/v1/products/decrease-stock")
                    .bodyValue(stockCheckItems)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            order.setStatus("CONFIRMED");
        } else {
            order.setStatus("PAYMENT_FAILED");
        }

        Order finalOrder = orderRepository.save(order);

        return mapToOrderResponse(finalOrder);

    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public boolean processPayment(Order order) {
        log.info("Attempting payment for order {}", order.getId());
        webClientBuilder.build().post()
                .uri(paymentServiceUrl + "/api/v1/payments")
                .bodyValue(order.getId()) // Send order ID or amount
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return true;
    }

    public boolean paymentFallback(Order order, Throwable t) {
        log.error("Payment service fallback executed for order {}. Error: {}", order.getId(), t.getMessage());
        return false;
    }

    private Order createAndSaveInitialOrder(Long userId, List<StockCheckResponse> productDetails, OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (StockCheckResponse productDetail : productDetails) {
            int quantity = orderRequest.items().stream()
                    .filter(item -> item.productId().equals(productDetail.productId()))
                    .findFirst().get().quantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productDetail.productId());
            orderItem.setQuantity(quantity);
            orderItem.setPricePerUnit(productDetail.price());
            order.addOrderItem(orderItem);

            totalAmount = totalAmount.add(productDetail.price().multiply(BigDecimal.valueOf(quantity)));
        }
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(),
                        "Product Name Placeholder",
                        item.getQuantity(),
                        item.getPricePerUnit()))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                itemResponses
        );
    }
}
