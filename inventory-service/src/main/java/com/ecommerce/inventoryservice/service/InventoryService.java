package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        // Logic: Tìm sản phẩm và kiểm tra số lượng > 0
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}