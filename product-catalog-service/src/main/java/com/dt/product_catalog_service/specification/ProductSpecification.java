package com.dt.product_catalog_service.specification;

import com.dt.product_catalog_service.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductSpecification {
    // Specification to filter by product name (case-insensitive)
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("productName")), name.toLowerCase());
    }

    // Specification to filter by product category (case-insensitive)
    public static Specification<Product> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("productCategory")), category.toLowerCase());
    }

    // Specification to filter by price less than or equal to a value
    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("productPrice"), maxPrice);
    }

    // Specification to filter by price greater than or equal to a value
    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("productPrice"), minPrice);
    }
}
