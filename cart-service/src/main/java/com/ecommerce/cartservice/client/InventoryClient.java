package com.ecommerce.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    // Gọi API kiểm tra tồn kho bên Inventory
    @GetMapping("/api/inventory/{sku-code}")
    boolean isInStock(@PathVariable("sku-code") String skuCode);
}