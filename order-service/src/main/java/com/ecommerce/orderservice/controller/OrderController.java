package com.ecommerce.orderservice.controller;

import com.ecommerce.commonlibrary.response.ResponseData; // Import common
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. Đặt hàng (User)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseData<String> placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
        String result = orderService.placeOrder(orderRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(), result, null);
    }

    // 2. Xem lịch sử đơn hàng của chính mình (User)
    @GetMapping("/my-orders")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<Order>> getMyOrders() {
        List<Order> orders = orderService.getMyOrders();
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy lịch sử đơn hàng thành công", orders);
    }

    // 3. Xem tất cả đơn hàng (Admin)
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách toàn bộ đơn hàng thành công", orders);
    }

    // 4. API Nội bộ (Cho Review Service gọi)
    // Lưu ý: API nội bộ thì trả về boolean thô (raw) để Feign bên kia dễ hứng, không cần bọc ResponseData
    @GetMapping("/has-purchased")
    @ResponseStatus(HttpStatus.OK)
    public boolean hasPurchased(@RequestParam("userId") String userId,
                                @RequestParam("skuCode") String skuCode) {
        return orderService.hasPurchased(userId, skuCode);
    }
}