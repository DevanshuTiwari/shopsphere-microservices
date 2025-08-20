package com.dt.product_catalog_service.service;

import com.dt.product_catalog_service.dto.StockCheckRequest;
import com.dt.product_catalog_service.dto.StockCheckResponse;
import com.dt.product_catalog_service.exception.InsufficientStockException;
import com.dt.product_catalog_service.exception.ResourceNotFoundException;
import com.dt.product_catalog_service.model.Inventory;
import com.dt.product_catalog_service.dto.ProductCreateRequest;
import com.dt.product_catalog_service.dto.ProductResponse;
import com.dt.product_catalog_service.model.Product;
import com.dt.product_catalog_service.repository.InventoryRepository;
import com.dt.product_catalog_service.repository.ProductRepository;
import com.dt.product_catalog_service.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
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
        if (name != null && !name.isEmpty()) {
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

    @Override
    public List<StockCheckResponse> checkStockAndGetDetails(List<StockCheckRequest> items) {

        List<StockCheckResponse> responses = new ArrayList<>();

        for (StockCheckRequest item : items) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + item.productId()));

            Inventory inventory = product.getInventory();
            boolean hasEnoughStock = inventory.getProductStockCount() >= item.quantity();

            StockCheckResponse response = new StockCheckResponse(
                    product.getId(),
                    hasEnoughStock,
                    product.getProductName(),
                    product.getProductPrice()
            );

            responses.add(response);
        }
        return responses;
    }

    @Override
    @Transactional
    public void decreaseStock(List<StockCheckRequest> items) {
        for (StockCheckRequest item : items) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + item.productId()));

            Inventory inventory = product.getInventory();

            if (inventory.getProductStockCount() < item.quantity()) {
                throw new InsufficientStockException("Insufficient stock for product id: " + item.productId());
            }

            inventory.setProductStockCount(inventory.getProductStockCount() - item.quantity());
        }
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
