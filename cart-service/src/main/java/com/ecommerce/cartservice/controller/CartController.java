package com.ecommerce.cartservice.controller;

import com.ecommerce.commonlibrary.response.ResponseData;
import com.ecommerce.cartservice.dto.CartItemDto;
import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseData<String> addToCart(@RequestBody @Valid CartItemDto cartItemDto) {
        cartService.addToCart(cartItemDto);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Đã thêm vào giỏ!", null);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<Cart> getMyCart() {
        Cart cart = cartService.getMyCart();
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy giỏ hàng thành công", cart);
    }

    @DeleteMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<String> removeFromCart(@PathVariable String skuCode) {
        cartService.removeFromCart(skuCode);
        return new ResponseData<>(HttpStatus.OK.value(), "Đã xóa sản phẩm khỏi giỏ!", null);
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<String> clearCart() {
        cartService.clearCart();
        return new ResponseData<>(HttpStatus.OK.value(), "Đã làm sạch giỏ hàng!", null);
    }
}