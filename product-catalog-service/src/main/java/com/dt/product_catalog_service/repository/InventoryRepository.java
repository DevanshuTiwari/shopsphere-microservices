package com.dt.product_catalog_service.repository;

import com.dt.product_catalog_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
