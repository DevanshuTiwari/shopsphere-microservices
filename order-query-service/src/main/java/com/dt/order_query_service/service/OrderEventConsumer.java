package com.dt.order_query_service.service;

import com.dt.order_query_service.event.OrderConfirmationEvent;


public interface OrderEventConsumer {
    void consume(OrderConfirmationEvent orderConfirmationEvent);
}
