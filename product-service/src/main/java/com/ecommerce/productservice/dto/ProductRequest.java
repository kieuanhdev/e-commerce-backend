package com.ecommerce.productservice.dto;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, max = 200, message = "Tên sản phẩm phải từ 3 đến 200 ký tự")
    private String name;

    @NotNull(message = "Giá sản phẩm là bắt buộc")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal price;

    // Không cần @NotNull vì boolean có thể null (sẽ handle default ở service)
    private Boolean isVisible;

    @Min(value = 0, message = "Số lượng tồn kho không được âm")
    private Integer quantity;

    @Min(value = 0, message = "Ngưỡng báo động kho không được âm")
    private Integer lowStockThreshold;

    // @URL(message = "Link ảnh không đúng định dạng") // Tùy chọn, nếu muốn check kỹ
    private String imageUrl;

    @Size(max = 500, message = "Mô tả ngắn không được quá 500 ký tự")
    private String shortDescription;

    private String longDescription;

    @NotBlank(message = "Danh mục (Category ID) là bắt buộc")
    private String categoryId;
}