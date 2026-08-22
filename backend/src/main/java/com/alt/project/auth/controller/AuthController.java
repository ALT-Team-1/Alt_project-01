package com.alt.project.auth.controller;

import com.alt.project.auth.dto.request.LoginRequest;
import com.alt.project.auth.dto.request.SignupRequest;
import com.alt.project.auth.dto.request.UserUpdateRequest;
import com.alt.project.auth.dto.response.SignupResponse;
import com.alt.project.auth.dto.response.TokenResponse;
import com.alt.project.auth.dto.response.UserResponse;
import com.alt.project.auth.service.AuthService;
import com.alt.project.auth.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(Authentication authentication) {
        UserResponse response = authService.getMyInfo(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(
            @RequestBody UserUpdateRequest request,
            Authentication authentication
    ) {
        UserResponse response = authService.updateMyInfo(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(Authentication authentication) {
        authService.deleteMyAccount(authentication.getName());
        return ResponseEntity.ok().build();
    }
}