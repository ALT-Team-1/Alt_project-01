package com.alt.project.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// auth에 대한 예외 처리 클래스
@Getter
public class AuthException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    private AuthException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static AuthException duplicateEmail() {
        return new AuthException(HttpStatus.CONFLICT, "USER_DUPLICATE_EMAIL", "이미 가입된 이메일입니다.");
    }

    public static AuthException invalidCredentials() {
        return new AuthException(HttpStatus.UNAUTHORIZED, "AUTH_INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다.");
    }
}