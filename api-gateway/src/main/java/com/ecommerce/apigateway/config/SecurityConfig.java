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
                        // Public endpoints
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/product/**").permitAll()

                        // 👇 THÊM MỚI: Cho phép Đăng ký & Quên mật khẩu tự do
                        .pathMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/forgot-password").permitAll()

                        // 👇 THÊM MỚI: Chỉ Admin được quản lý user (Xem/Xóa)
                        .pathMatchers("/api/users/**").hasRole("ADMIN")

                        // Quyền Admin cho Product (như cũ)
                        .pathMatchers(HttpMethod.POST, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN")

                        // Order & Inventory cần đăng nhập
                        .pathMatchers("/api/order/**").authenticated()
                        .pathMatchers("/api/inventory/**").authenticated()

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