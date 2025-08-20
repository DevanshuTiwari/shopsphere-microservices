package com.dt.order_service.service;

import com.dt.order_service.dto.OrderRequest;
import com.dt.order_service.dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(String userEmail, OrderRequest orderRequest);
}
