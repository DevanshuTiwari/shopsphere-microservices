package com.dt.order_query_service.repository;

import com.dt.order_query_service.model.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDocumentRepository extends MongoRepository<OrderDocument, String> {
}
