package com.dt.order_query_service.service;

import com.dt.order_query_service.model.OrderDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryService {

    Page<OrderDocument> getAllOrders(Pageable pageable);

    OrderDocument getOrderById(String orderId);
}
