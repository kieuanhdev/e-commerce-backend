package com.ecommerce.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {
    @NotBlank(message = "Mã sản phẩm (SkuCode) không được để trống")
    private String skuCode;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng nhập kho không được âm")
    private Integer quantity;
}