package com.dt.product_catalog_service.controller;


import com.dt.product_catalog_service.dto.ProductCreateRequest;
import com.dt.product_catalog_service.dto.ProductResponse;
import com.dt.product_catalog_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        return productService.createProduct(productCreateRequest);
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/search")
    public Page<ProductResponse> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable) {
        return productService.searchProducts(name, category, minPrice, maxPrice, pageable);
    }
}
