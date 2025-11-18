package com.ecommerce.reviewservice.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private String skuCode;
    private Double rating;
    private String comment;
}