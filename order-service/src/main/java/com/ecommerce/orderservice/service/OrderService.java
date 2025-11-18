package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.InventoryClient;
import com.ecommerce.orderservice.dto.OrderLineItemsDto;
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderLineItems;
import com.ecommerce.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    // Tên "inventory" phải trùng với config trong application.properties
    // 1. SỬA HÀM ĐẶT HÀNG (Lưu thêm userId)
    @CircuitBreaker(name = "inventory", fallbackMethod = "placeOrderFallback")
    public String placeOrder(OrderRequest orderRequest) {
        // Lấy thông tin User từ Security Context (Token)
        String userId = getCurrentUserId();

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(userId); // 👇 LƯU USER ID VÀO DB

        // 2. Map dữ liệu từ DTO (Request) sang Entity (Database)
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        // 3. Gọi Inventory Service để kiểm tra tồn kho
        // Logic: Lấy tất cả skuCode trong đơn hàng, hỏi Inventory xem có hàng không
        boolean allProductsInStock = orderLineItems.stream()
                .allMatch(item -> inventoryClient.checkStock(item.getSkuCode()));

        // 4. Nếu có hàng thì lưu, không thì báo lỗi
        if(allProductsInStock) {
            orderRepository.save(order); // Lúc này biến 'order' đã được khai báo ở bước 1 nên không lỗi nữa
            return "Đặt hàng thành công!";
        } else {
            throw new IllegalArgumentException("Sản phẩm không có trong kho, vui lòng thử lại sau");
        }
    }

    // 2. HÀM XEM LỊCH SỬ (User xem đơn của mình)
    public List<Order> getMyOrders() {
        String userId = getCurrentUserId();
        return orderRepository.findByUserId(userId);
    }

    // 3. HÀM QUẢN LÝ (Admin xem tất cả)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Hàm phụ trợ để lấy ID từ Token
    private String getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getSubject(); // Subject trong JWT chính là User ID (UUID)
    }

    // Hàm này chạy khi Inventory Service bị sập hoặc quá tải
    public String placeOrderFallback(OrderRequest orderRequest, Throwable runtimeException) {
        return "Rất tiếc! Hệ thống kho đang bận hoặc bảo trì. Vui lòng thử lại sau ít phút.";
    }

    // Hàm phụ để chuyển đổi dữ liệu
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    // 👇 THÊM HÀM NÀY: Hàm nghiệp vụ kiểm tra mua hàng
    public boolean hasPurchased(String userId, String skuCode) {
        return orderRepository.existsByUserIdAndSkuCode(userId, skuCode);
    }

}