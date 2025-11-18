package com.ecommerce.cartservice.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDto {
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
}