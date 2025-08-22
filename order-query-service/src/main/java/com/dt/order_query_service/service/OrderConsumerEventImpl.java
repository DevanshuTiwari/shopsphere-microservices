package com.dt.order_query_service.service;

import com.dt.order_query_service.event.OrderConfirmationEvent;
import com.dt.order_query_service.model.OrderDocument;
import com.dt.order_query_service.model.OrderItem;
import com.dt.order_query_service.repository.OrderDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderConsumerEventImpl implements OrderEventConsumer {

    private final OrderDocumentRepository orderDocumentRepository;

    @Autowired
    public OrderConsumerEventImpl(OrderDocumentRepository orderDocumentRepository) {
        this.orderDocumentRepository = orderDocumentRepository;
    }

    @Override
    public void consume(OrderConfirmationEvent orderConfirmationEvent) {
        log.info("Received Order Confirmation Event for order ID: {}", orderConfirmationEvent.orderId());

        try {
            OrderDocument orderDocument = new OrderDocument();

            orderDocument.setOrderId(orderConfirmationEvent.orderId());
            orderDocument.setCustomerFirstName(orderConfirmationEvent.customerFirstName());
            orderDocument.setCustomerLastName(orderConfirmationEvent.customerLastName());
            orderDocument.setTotalAmount(orderConfirmationEvent.totalAmount());
            orderDocument.setStatus("CONFIRMED");
            orderDocument.setOrderDate(LocalDateTime.now());

            List<OrderItem> orderItem = orderConfirmationEvent.items().stream()
                    .map(orderItemDTO -> new OrderItem(
                            orderItemDTO.productId(),
                            orderItemDTO.quantity(),
                            orderItemDTO.price()
                    )).toList();

            orderDocument.setItems(orderItem);

            orderDocumentRepository.save(orderDocument);
            log.info("Successfully saved order document for order ID: {}", orderConfirmationEvent.orderId());
        } catch (Exception e) {
            log.error("Error processing order confirmation event for order ID: {}", orderConfirmationEvent.orderId(), e);
        }
    }
}
