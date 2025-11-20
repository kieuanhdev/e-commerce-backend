package com.ecommerce.cartservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDto {
    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String skuCode;

    @NotNull(message = "Số lượng là bắt buộc")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Giá tiền là bắt buộc")
    @Min(value = 0, message = "Giá tiền không được âm")
    private BigDecimal price;
}