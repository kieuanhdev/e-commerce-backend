package com.ecommerce.inventoryservice.repository;

import com.ecommerce.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Tìm kiếm kho theo mã sản phẩm
    Optional<Inventory> findBySkuCode(String skuCode);
}