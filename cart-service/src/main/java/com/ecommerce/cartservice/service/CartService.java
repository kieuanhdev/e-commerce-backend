package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.client.InventoryClient;
import com.ecommerce.cartservice.dto.CartItemDto;
import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.model.CartItem;
import com.ecommerce.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final InventoryClient inventoryClient;

    // 1. Thêm vào giỏ
    public void addToCart(CartItemDto cartItemDto) {
        String userId = getUserId();

        // Check kho
        if (!inventoryClient.isInStock(cartItemDto.getSkuCode())) {
            throw new RuntimeException("Sản phẩm đã hết hàng!");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> Cart.builder().userId(userId).build());

        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getSkuCode().equals(cartItemDto.getSkuCode()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Nếu có rồi -> Cộng dồn số lượng
            existingItem.get().setQuantity(existingItem.get().getQuantity() + cartItemDto.getQuantity());
        } else {
            // Nếu chưa -> Thêm mới
            CartItem newItem = CartItem.builder()
                    .skuCode(cartItemDto.getSkuCode())
                    .price(cartItemDto.getPrice())
                    .quantity(cartItemDto.getQuantity())
                    .build();
            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    // 2. Lấy giỏ hàng của User
    public Cart getMyCart() {
        return cartRepository.findByUserId(getUserId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống"));
    }

    // 3. Xóa sản phẩm khỏi giỏ
    public void removeFromCart(String skuCode) {
        Cart cart = getMyCart();
        cart.getCartItems().removeIf(item -> item.getSkuCode().equals(skuCode));
        cartRepository.save(cart);
    }

    // 4. Xóa sạch giỏ hàng (Sau khi Order thành công)
    public void clearCart() {
        Cart cart = getMyCart();
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private String getUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getSubject();
    }
}