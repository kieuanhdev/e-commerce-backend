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
                        // 1. Cho phép xem (GET) sản phẩm thoải mái (Ai cũng xem được)
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/product/**").permitAll()

                        // 2. QUYỀN ADMIN: Chỉ Admin mới được Thêm (POST), Sửa, Xóa sản phẩm
                        .pathMatchers(HttpMethod.POST, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN")

                        // 3. Đặt hàng vẫn yêu cầu đăng nhập (User thường cũng được)
                        .pathMatchers("/api/order/**").authenticated()

                        .anyExchange().authenticated()
                )
                // Kích hoạt Converter để đọc Role
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(makePermissionsConverter()))
                );

        return serverHttpSecurity.build();
    }

    // Hàm cấu hình Converter
    private ReactiveJwtAuthenticationConverter makePermissionsConverter() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return jwtAuthenticationConverter;
    }
}