package com.ecommerce.userservice.controller;

import com.ecommerce.commonlibrary.response.ResponseData;
import com.ecommerce.userservice.dto.UserRegistrationRequest;
import com.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. API Admin xem danh sách
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<UserRepresentation>> findAllUsers() {
        List<UserRepresentation> users = userService.findAllUsers();
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách thành công", users);
    }

    // 2. API Đăng ký (Thêm @Valid)
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseData<String> register(@RequestBody @Valid UserRegistrationRequest request) {
        // Service trả về String (thông báo thành công) hoặc ném Exception
        userService.createUser(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Đăng ký tài khoản thành công", null);
    }

    // 3. API Quên mật khẩu
    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<String> forgotPassword(@RequestParam String email) {
        // Nếu email rỗng, lỗi sẽ được bắt thủ công hoặc dùng @NotBlank nếu chuyển sang DTO
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email không được để trống");
        }

        userService.forgotPassword(email);
        return new ResponseData<>(HttpStatus.OK.value(), "Vui lòng kiểm tra Email để đặt lại mật khẩu!", null);
    }
}