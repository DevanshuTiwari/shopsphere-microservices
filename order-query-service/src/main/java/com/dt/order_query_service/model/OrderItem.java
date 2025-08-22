package com.dt.order_query_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long productId;
    private Integer quantity;
    private BigDecimal pricePerUnit;
}
