package com.dt.order_query_service.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long productId,
        Integer quantity,
        BigDecimal price
) {
}
