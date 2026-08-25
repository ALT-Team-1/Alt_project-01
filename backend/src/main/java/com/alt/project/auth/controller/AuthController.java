package com.alt.project.auth.controller;

import com.alt.project.auth.dto.request.LoginRequest;
import com.alt.project.auth.dto.request.SignupRequest;
import com.alt.project.auth.dto.request.UserUpdateRequest;
import com.alt.project.auth.dto.response.SignupResponse;
import com.alt.project.auth.dto.response.TokenResponse;
import com.alt.project.auth.dto.response.UserResponse;
import com.alt.project.auth.service.AuthService;
import com.alt.project.global.api.ApiResponse;
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

    // 올바른 이메일과 비번 등의 요청이 왔는 지 유효성 검사하고 성공 시 201 코드와 리스폰스 객체를 반환
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    // 올바른 이메일과 비번 요청이 왔는 지 유효성 검사하고 성공 시 jwt 토큰 발급 결과를 담아 200 코드와 반환됨
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 인증된 유저의 정보를 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(Authentication authentication) {
        UserResponse response = authService.getMyInfo(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 인증된 유저의 정보를 수정
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(@Valid @RequestBody UserUpdateRequest request,
            Authentication authentication
    ) {
        UserResponse response = authService.updateMyInfo(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 인증된 유저의 계정을 삭제
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(Authentication authentication) {
        authService.deleteMyAccount(authentication.getName());
        return ResponseEntity.ok().build();
    }
}