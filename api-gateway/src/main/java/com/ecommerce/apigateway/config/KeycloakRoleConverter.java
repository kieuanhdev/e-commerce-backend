package com.ecommerce.apigateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 👇 SỬA ĐỔI: Implement Converter trả về Flux<GrantedAuthority> thay vì Collection
public class KeycloakRoleConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        // 1. Lấy phần "realm_access" từ Token
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return Flux.empty(); // Trả về luồng rỗng nếu không có role
        }

        // 2. Lấy danh sách roles
        List<GrantedAuthority> authorities = ((List<String>) realmAccess.get("roles"))
                .stream()
                .map(roleName -> "ROLE_" + roleName) // Thêm tiền tố ROLE_
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 3. 👇 QUAN TRỌNG: Chuyển List thành Flux để Spring Security WebFlux hiểu được
        return Flux.fromIterable(authorities);
    }
}