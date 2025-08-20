package com.dt.product_catalog_service.dto;

public record StockCheckRequest(
        Long productId,
        Integer quantity
) {}
