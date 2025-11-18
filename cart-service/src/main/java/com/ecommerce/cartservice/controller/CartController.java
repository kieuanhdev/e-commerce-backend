package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.CartItemDto;
import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.service.CartService;
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
    public String addToCart(@RequestBody CartItemDto cartItemDto) {
        cartService.addToCart(cartItemDto);
        return "Đã thêm vào giỏ!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Cart getMyCart() {
        return cartService.getMyCart();
    }

    @DeleteMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public String removeFromCart(@PathVariable String skuCode) {
        cartService.removeFromCart(skuCode);
        return "Đã xóa sản phẩm khỏi giỏ!";
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public String clearCart() {
        cartService.clearCart();
        return "Đã làm sạch giỏ hàng!";
    }
}