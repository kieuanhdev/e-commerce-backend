package com.ecommerce.reviewservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// name="order-service": Phải trùng với tên đăng ký Eureka của Order Service
@FeignClient(name = "order-service")
public interface OrderClient {

    // Gọi API nội bộ mà bạn vừa tạo ở Order Service
    @GetMapping("/api/order/has-purchased")
    boolean hasPurchased(@RequestParam("userId") String userId,
                         @RequestParam("skuCode") String skuCode);
}