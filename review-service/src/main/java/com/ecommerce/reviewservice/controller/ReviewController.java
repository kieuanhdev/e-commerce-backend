package com.ecommerce.reviewservice.controller;

import com.ecommerce.commonlibrary.response.ResponseData;
import com.ecommerce.reviewservice.dto.ReviewRequest;
import com.ecommerce.reviewservice.model.Review;
import com.ecommerce.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseData<String> addReview(@RequestBody @Valid ReviewRequest request) {
        reviewService.addReview(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Đánh giá thành công!", null);
    }

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<Review>> getReviews(@PathVariable String skuCode) {
        List<Review> reviews = reviewService.getReviewsByProduct(skuCode);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách đánh giá thành công", reviews);
    }
}