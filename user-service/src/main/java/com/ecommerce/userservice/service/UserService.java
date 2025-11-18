package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.UserRegistrationRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    // 1. Lấy danh sách User (Cho Admin)
    public List<UserRepresentation> findAllUsers() {
        return keycloak.realm(realm).users().list();
    }

    // 2. Đăng ký User mới (Cho Khách)
    public String createUser(UserRegistrationRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(false);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);

        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() == 201) {
            return "Đăng ký thành công!";
        } else {
            throw new RuntimeException("Đăng ký thất bại. Mã lỗi: " + response.getStatus());
        }
    }

    // 3. Quên mật khẩu (Gửi Email)
    public void forgotPassword(String email) {
        List<UserRepresentation> users = keycloak.realm(realm).users().searchByEmail(email, true);
        if (users.isEmpty()) {
            throw new RuntimeException("Email không tồn tại!");
        }
        // Gửi email yêu cầu đổi pass (Cần cấu hình Mailtrap trong Keycloak trước)
        keycloak.realm(realm).users().get(users.get(0).getId())
                .executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }
}