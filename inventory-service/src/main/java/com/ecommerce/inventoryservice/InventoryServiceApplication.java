package com.ecommerce.inventoryservice;

import com.ecommerce.inventoryservice.model.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    // 👇 THÊM ĐOẠN NÀY: Tự động chạy khi app khởi động
    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            // Chỉ tạo dữ liệu nếu kho đang trống
            if(inventoryRepository.count() == 0) {
                Inventory inventory = new Inventory();
                inventory.setSkuCode("iphone_15");
                inventory.setQuantity(100);

                Inventory inventory2 = new Inventory();
                inventory2.setSkuCode("iphone_15_red");
                inventory2.setQuantity(0);

                inventoryRepository.save(inventory);
                inventoryRepository.save(inventory2);
            }
        };
    }
}