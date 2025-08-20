package com.dt.product_catalog_service.service;

import com.dt.product_catalog_service.dto.ProductCreateRequest;
import com.dt.product_catalog_service.dto.ProductResponse;
import com.dt.product_catalog_service.dto.StockCheckRequest;
import com.dt.product_catalog_service.dto.StockCheckResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest productCreateRequest);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    List<StockCheckResponse> checkStockAndGetDetails(List<StockCheckRequest> items);

    void decreaseStock(List<StockCheckRequest> items);
}
