package com.ecommerce.reviewservice.repository;

import com.ecommerce.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Lấy tất cả review của 1 sản phẩm
    List<Review> findBySkuCode(String skuCode);

    // Kiểm tra xem user này đã review sản phẩm này chưa (tránh spam)
    boolean existsByUserIdAndSkuCode(String userId, String skuCode);
}