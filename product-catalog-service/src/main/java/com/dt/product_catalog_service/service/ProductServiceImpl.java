package com.dt.product_catalog_service.service;

import com.dt.product_catalog_service.model.Inventory;
import com.dt.product_catalog_service.dto.ProductCreateRequest;
import com.dt.product_catalog_service.dto.ProductResponse;
import com.dt.product_catalog_service.model.Product;
import com.dt.product_catalog_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



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
        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getProductName(),
                savedProduct.getProductDescription(),
                savedProduct.getProductCategory(),
                savedProduct.getProductPrice(),
                savedProduct.getInventory().getProductStockCount()
        );
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getProductName(),
                        product.getProductDescription(),
                        product.getProductCategory(),
                        product.getProductPrice(),
                        product.getInventory().getProductStockCount()
                ));
    }
}
