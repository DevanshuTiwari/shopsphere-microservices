package com.dt.order_service.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long productId,
        Integer quantity,
        BigDecimal price
) {
}
