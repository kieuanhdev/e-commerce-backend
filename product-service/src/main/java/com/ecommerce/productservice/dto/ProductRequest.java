package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private Boolean isVisible;
    private Integer quantity;
    private Integer lowStockThreshold;
    private String imageUrl;
    private String shortDescription;
    private String longDescription;
    private String categoryId;
}