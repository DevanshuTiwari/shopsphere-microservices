package com.dt.product_catalog_service.dto;

import java.math.BigDecimal;

public record ProductCreateRequest(
        String name,
        String description,
        BigDecimal price,
        String category,
        Integer initialStockCount
) {}
