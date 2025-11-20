package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryRequest;
import com.ecommerce.inventoryservice.model.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // 1. Kiểm tra hàng (Giữ nguyên để Order/Cart Service gọi)
    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    // 2. Nhập kho (Hàm mới)
    @Transactional
    public void updateStock(InventoryRequest request) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findBySkuCode(request.getSkuCode());

        if (inventoryOptional.isPresent()) {
            // Nếu đã có -> Cộng dồn số lượng
            Inventory inventory = inventoryOptional.get();
            inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
            inventoryRepository.save(inventory);
        } else {
            // Nếu chưa có -> Tạo mới
            Inventory newInventory = Inventory.builder()
                    .skuCode(request.getSkuCode())
                    .quantity(request.getQuantity())
                    .build();
            inventoryRepository.save(newInventory);
        }
    }

    // 3. Xem tồn kho của 1 sản phẩm
    public Integer getStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .map(Inventory::getQuantity)
                .orElse(0);
    }
}