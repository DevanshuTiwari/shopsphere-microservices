package com.dt.order_service.dto;


import java.util.List;

public record OrderRequest(
        List<OrderItemRequest> items
){}
