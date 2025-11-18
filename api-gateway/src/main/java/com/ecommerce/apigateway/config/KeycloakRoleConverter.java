package com.ecommerce.apigateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Class này chuyển đổi Role từ Keycloak sang chuẩn Spring Security (Reactive)
public class KeycloakRoleConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        // 1. Lấy phần "realm_access" từ trong Token
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return Flux.empty();
        }

        // 2. Lấy danh sách roles (VD: ["admin", "user"])
        List<GrantedAuthority> authorities = ((List<String>) realmAccess.get("roles"))
                .stream()
                .map(roleName -> "ROLE_" + roleName) // Thêm tiền tố ROLE_ thành ROLE_ADMIN
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 3. Trả về Flux (Dòng chảy dữ liệu) thay vì List tĩnh
        return Flux.fromIterable(authorities);
    }
}