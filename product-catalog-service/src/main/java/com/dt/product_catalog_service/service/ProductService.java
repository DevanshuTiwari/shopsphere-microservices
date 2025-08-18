package com.dt.product_catalog_service.service;

import com.dt.product_catalog_service.dto.ProductCreateRequest;
import com.dt.product_catalog_service.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest productCreateRequest);

    Page<ProductResponse> getAllProducts(Pageable pageable);
}
