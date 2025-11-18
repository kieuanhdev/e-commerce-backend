package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. Đặt hàng (Lưu userId ngầm)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    // 2. User xem lịch sử đơn hàng của chính mình
    @GetMapping("/my-orders")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getMyOrders() {
        return orderService.getMyOrders();
    }

    // 3. Admin xem tất cả đơn hàng
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}