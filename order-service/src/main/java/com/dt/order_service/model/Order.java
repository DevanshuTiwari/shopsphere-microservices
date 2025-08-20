package com.dt.order_service.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "status", nullable = false)
    private String status; // e.g., PENDING, CONFIRMED, SHIPPED, DELIVERED

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    // One order can have many order items
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    // Helper method to maintain the bidirectional link
    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
