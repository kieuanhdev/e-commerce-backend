package com.ecommerce.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // --- 1. PUBLIC (Ai cũng vào được) ---
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/reviews/**").permitAll() // Xem review thoải mái
                        .pathMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/forgot-password").permitAll()

                        // --- 2. ADMIN ONLY (Chỉ Sếp mới được vào) ---
                        .pathMatchers("/api/users/**").hasRole("ADMIN") // Quản lý User (Xem list)
                        .pathMatchers("/api/order/all").hasRole("ADMIN") // Xem tất cả đơn hàng
                        // Quản lý sản phẩm (Thêm/Sửa/Xóa)
                        .pathMatchers(HttpMethod.POST, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN")

                        // --- 3. USER (Phải đăng nhập - Role nào cũng được) ---

                        // CART SERVICE (Giỏ hàng) - Cấu hình mới thêm
                        .pathMatchers("/api/cart/**").authenticated()

                        // ORDER SERVICE (Đặt hàng & Xem đơn của mình)
                        .pathMatchers("/api/order/my-orders").authenticated()
                        .pathMatchers("/api/order").authenticated()

                        // INVENTORY SERVICE (Kiểm tra kho)
                        .pathMatchers("/api/inventory/**").authenticated()

                        // REVIEW SERVICE (Viết đánh giá)
                        .pathMatchers(HttpMethod.POST, "/api/reviews").authenticated()

                        // --- 4. CHỐT CHẶN CUỐI CÙNG ---
                        // Bất kỳ đường dẫn nào chưa khai báo ở trên đều bắt buộc phải đăng nhập
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(makePermissionsConverter()))
                );

        return serverHttpSecurity.build();
    }

    // Hàm cấu hình Converter để đọc Role từ Keycloak
    private ReactiveJwtAuthenticationConverter makePermissionsConverter() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return jwtAuthenticationConverter;
    }
}