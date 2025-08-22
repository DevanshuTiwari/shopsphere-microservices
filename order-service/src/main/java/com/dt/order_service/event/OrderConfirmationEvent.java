package com.dt.order_service.event;

import com.dt.order_service.dto.OrderItemDTO;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmationEvent(
        String orderId,
        String customerEmail,
        String customerFirstName,
        String customerLastName,
        BigDecimal totalAmount,
        List<OrderItemDTO> items
) {
}
