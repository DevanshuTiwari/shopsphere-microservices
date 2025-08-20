package com.dt.order_service.dto;

public record StockCheckRequest(
        Long productId,
        Integer quantity
) {}
