package com.dt.order_service.contoller;

import com.dt.order_service.dto.OrderRequest;
import com.dt.order_service.dto.OrderResponse;
import com.dt.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            Authentication authentication) {

        String userEmail = authentication.getName();
        return orderService.placeOrder(userEmail, orderRequest);
    }
}
