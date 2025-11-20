package com.ecommerce.reviewservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String skuCode;

    @NotNull(message = "Điểm đánh giá là bắt buộc")
    @Min(value = 1, message = "Đánh giá thấp nhất là 1 sao")
    @Max(value = 5, message = "Đánh giá cao nhất là 5 sao")
    private Double rating;

    @Size(max = 500, message = "Nội dung đánh giá không được quá 500 ký tự")
    private String comment;
}