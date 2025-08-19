package com.dt.product_catalog_service.service;

import com.dt.product_catalog_service.model.Inventory;
import com.dt.product_catalog_service.dto.ProductCreateRequest;
import com.dt.product_catalog_service.dto.ProductResponse;
import com.dt.product_catalog_service.model.Product;
import com.dt.product_catalog_service.repository.ProductRepository;
import com.dt.product_catalog_service.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {

        // DTO to Entity
        Product product = new Product();
        product.setProductName(productCreateRequest.name());
        product.setProductDescription(productCreateRequest.description());
        product.setProductPrice(productCreateRequest.price());
        product.setProductCategory(productCreateRequest.category());

        Inventory inventory = new Inventory();
        inventory.setProductStockCount(productCreateRequest.initialStockCount());

        product.setInventory(inventory);
        inventory.setProduct(product);

        Product savedProduct = productRepository.save(product);

        // Entity to DTO Response
        return mapToProductResponse(savedProduct);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToProductResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        // Base specification that matches everything
        Specification<Product> productSpecification = Specification.unrestricted();

        // Search criteria on the basis of name
        if(name != null && !name.isEmpty()){
            productSpecification = productSpecification.and(ProductSpecification.hasName(name));
        }

        // Search criteria on the basis of category
        if (category != null && !category.isEmpty()) {
            productSpecification = productSpecification.and(ProductSpecification.hasCategory(category));
        }

        // Search criteria on the basis of minimum price
        if (minPrice != null) {
            productSpecification = productSpecification.and(ProductSpecification.priceGreaterThanOrEqual(minPrice));
        }

        // Search criteria on the basis of maximum price
        if (minPrice != null) {
            productSpecification = productSpecification.and(ProductSpecification.priceLessThanOrEqual(maxPrice));
        }

        return productRepository.findAll(productSpecification, pageable)
                .map(this::mapToProductResponse);
    }

    // Helper method to map product response
    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getProductDescription(),
                product.getProductCategory(),
                product.getProductPrice(),
                product.getInventory().getProductStockCount()
        );
    }
}
