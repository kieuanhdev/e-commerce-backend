package com.ecommerce.inventoryservice.controller;

import com.ecommerce.commonlibrary.response.ResponseData;
import com.ecommerce.inventoryservice.dto.InventoryRequest;
import com.ecommerce.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // 1. API Nội bộ (Order/Cart gọi) - GIỮ NGUYÊN kiểu trả về boolean
    // Đừng sửa thành ResponseData để tránh break code các service khác
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    // 2. API Nhập kho (Admin dùng) - Dùng ResponseData
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<String> updateStock(@RequestBody @Valid InventoryRequest request) {
        inventoryService.updateStock(request);
        return new ResponseData<>(HttpStatus.OK.value(), "Nhập kho thành công!", null);
    }

    // 3. API Xem tồn kho (Admin dùng) - Dùng ResponseData
    @GetMapping("/{sku-code}/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<Integer> getStockCount(@PathVariable("sku-code") String skuCode) {
        Integer quantity = inventoryService.getStock(skuCode);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin tồn kho thành công", quantity);
    }
}