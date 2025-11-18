package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.UserRegistrationRequest;
import com.ecommerce.userservice.service.UserService;
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

    // Chỉ Admin được gọi
    @GetMapping
    public List<UserRepresentation> findAllUsers() {
        return userService.findAllUsers();
    }

    // Ai cũng gọi được
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody UserRegistrationRequest request) {
        return userService.createUser(request);
    }

    // Ai cũng gọi được
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return "Vui lòng kiểm tra Email để đặt lại mật khẩu!";
    }
}