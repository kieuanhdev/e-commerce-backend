package com.ecommerce.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotEmpty(message = "Đơn hàng phải có ít nhất 1 sản phẩm")
    @Valid // Quan trọng: Để nó validate tiếp bên trong list
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}