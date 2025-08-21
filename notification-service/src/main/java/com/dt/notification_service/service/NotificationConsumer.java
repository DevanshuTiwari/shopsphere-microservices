package com.dt.notification_service.service;

import com.dt.notification_service.event.OrderConfirmationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "notificationTopic", groupId = "notification-group-id")
    public void consume(OrderConfirmationEvent event) {
        log.info("Received Order Confirmation Event: {}", event);

        // TODO: Add logic here to send email using Java Mail Sender
        log.info("Preparing to send confirmation email to {} for order {}", event.customerEmail(), event.orderId());
        System.out.println("Email would be sent to: " + event.customerEmail());
    }
}
