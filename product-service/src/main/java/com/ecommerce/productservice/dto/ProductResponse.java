package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private Boolean isVisible;
    private Integer lowStockThreshold;
    private String imageUrl;
    private String shortDescription;
    private String longDescription;
    private String categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}