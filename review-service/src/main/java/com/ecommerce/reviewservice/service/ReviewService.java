package com.ecommerce.reviewservice.service;

import com.ecommerce.reviewservice.client.OrderClient;
import com.ecommerce.reviewservice.dto.ReviewRequest;
import com.ecommerce.reviewservice.model.Review;
import com.ecommerce.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderClient orderClient;

    public void addReview(ReviewRequest request) {
        // 1. Lấy User ID từ Token hiện tại
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getSubject(); // Lấy ID của user (UUID)

        // 2. Gọi Order Service để kiểm tra: "User này đã mua hàng chưa?"
        boolean hasPurchased = orderClient.hasPurchased(userId, request.getSkuCode());

        if (!hasPurchased) {
            throw new RuntimeException("Bạn chưa mua sản phẩm này nên không thể đánh giá!");
        }

        // 3. Kiểm tra xem đã review chưa
        if (reviewRepository.existsByUserIdAndSkuCode(userId, request.getSkuCode())) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi!");
        }

        // 4. Lưu Review
        Review review = Review.builder()
                .skuCode(request.getSkuCode())
                .userId(userId)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(String skuCode) {
        return reviewRepository.findBySkuCode(skuCode);
    }
}