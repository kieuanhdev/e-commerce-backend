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
                        // 1. Public Endpoints (Không cần đăng nhập)
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/forgot-password").permitAll()

                        // 2. Admin Endpoints (Cần Role ADMIN)
                        .pathMatchers("/api/users/**").hasRole("ADMIN") // Quản lý User
                        .pathMatchers("/api/order/all").hasRole("ADMIN") // Xem tất cả đơn
                        .pathMatchers(HttpMethod.POST, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/product/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN")

                        // 3. Authenticated Endpoints (Cần đăng nhập, Role gì cũng được)
                        .pathMatchers("/api/order/my-orders").authenticated() // Xem đơn của mình
                        .pathMatchers("/api/order").authenticated() // Đặt hàng
                        .pathMatchers("/api/inventory/**").authenticated()

                        // 👇 THÊM DÒNG NÀY: Cho phép xem review (GET) thoải mái
                        .pathMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()

                        // 👇 Bắt buộc đăng nhập khi viết review (POST)
                        .pathMatchers(HttpMethod.POST, "/api/reviews").authenticated()

                        // Chặn tất cả các đường dẫn lạ khác
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(makePermissionsConverter()))
                );

        return serverHttpSecurity.build();
    }

    // Hàm này sẽ hết báo đỏ khi bạn cập nhật file KeycloakRoleConverter ở trên
    private ReactiveJwtAuthenticationConverter makePermissionsConverter() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return jwtAuthenticationConverter;
    }
}