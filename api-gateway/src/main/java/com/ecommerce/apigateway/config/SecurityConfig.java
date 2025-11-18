package com.ecommerce.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Tắt CSRF
                .authorizeExchange(exchange -> exchange
                        // --- LUẬT PHÂN QUYỀN ---

                        // 1. Cho phép tự do truy cập Eureka và Xem sản phẩm
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/product/**").permitAll()

                        // 2. Bắt buộc đăng nhập mới được vào Đặt hàng và Kho
                        .pathMatchers("/api/order/**").authenticated()
                        .pathMatchers("/api/inventory/**").authenticated()

                        // Mặc định các API khác cũng phải đăng nhập
                        .anyExchange().authenticated()
                )
                // Kích hoạt xác thực JWT với Keycloak
                .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()));

        return serverHttpSecurity.build();
    }
}