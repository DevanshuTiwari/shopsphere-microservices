package com.dt.order_service.dto;

import java.math.BigDecimal;

public record StockCheckResponse(
        Long productId,
        boolean inStock,
        String productName,
        BigDecimal price
) {}
