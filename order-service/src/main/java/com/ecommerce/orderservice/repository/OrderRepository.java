package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);

    // Tìm xem có đơn hàng nào của user này chứa sản phẩm này không
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM Order o JOIN o.orderLineItemsList i " +
            "WHERE o.userId = :userId AND i.skuCode = :skuCode")
    boolean existsByUserIdAndSkuCode(String userId, String skuCode);
}