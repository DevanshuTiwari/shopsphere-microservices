package com.dt.order_query_service.service;


import com.dt.order_query_service.exception.ResourceNotFoundException;
import com.dt.order_query_service.model.OrderDocument;
import com.dt.order_query_service.repository.OrderDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderDocumentRepository orderDocumentRepository;

    @Autowired
    public OrderQueryServiceImpl(OrderDocumentRepository orderDocumentRepository) {
        this.orderDocumentRepository = orderDocumentRepository;
    }

    @Override
    public Page<OrderDocument> getAllOrders(Pageable pageable) {
        return orderDocumentRepository.findAll(pageable);
    }

    @Override
    public OrderDocument getOrderById(String orderId) {
        return orderDocumentRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }
}
