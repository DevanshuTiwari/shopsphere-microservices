package com.dt.product_catalog_service.dto;

import java.math.BigDecimal;

public record StockCheckResponse(
        Long productId,
        boolean inStock,
        String productName,
        BigDecimal price
) {}
