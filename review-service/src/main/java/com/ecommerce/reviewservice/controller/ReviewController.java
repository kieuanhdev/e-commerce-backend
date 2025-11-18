package com.ecommerce.reviewservice.controller;

import com.ecommerce.reviewservice.dto.ReviewRequest;
import com.ecommerce.reviewservice.model.Review;
import com.ecommerce.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // API Đánh giá (Cần đăng nhập)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addReview(@RequestBody ReviewRequest request) {
        reviewService.addReview(request);
        return "Đánh giá thành công!";
    }

    // API Xem đánh giá (Ai cũng xem được)
    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviews(@PathVariable String skuCode) {
        return reviewService.getReviewsByProduct(skuCode);
    }
}