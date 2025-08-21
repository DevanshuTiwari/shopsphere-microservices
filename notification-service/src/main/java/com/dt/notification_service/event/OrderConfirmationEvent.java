package com.dt.notification_service.event;

import java.math.BigDecimal;

public record OrderConfirmationEvent(
        String orderId,
        String customerEmail,
        String customerFirstName,
        String customerLastName,
        BigDecimal totalAmount
) {
}
