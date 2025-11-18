package com.ecommerce.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// name: Phải trùng tên với spring.application.name của Inventory Service
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    // Copy y nguyên dòng khai báo bên Controller của Inventory Service
    @GetMapping("/api/inventory/{sku-code}")
    boolean checkStock(@PathVariable("sku-code") String skuCode);
}