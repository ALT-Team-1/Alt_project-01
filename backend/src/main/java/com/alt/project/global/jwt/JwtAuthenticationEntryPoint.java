package com.alt.project.global.jwt;

import com.alt.project.global.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

// 인증이 필요한 API에 인증 안 된 상태로 접근이 불가능하게 함
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 인증되지 않은 요청이 자원에 접근하려 할 때 스프링 시큐리티가 자동으로 호출
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 응답을 수동으로 직접 구성
        response.setStatus(HttpStatus.UNAUTHORIZED.value());       // 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 제이슨 타입
        response.setCharacterEncoding("UTF-8");

        // 요청 경로에 따라 응답 형태를 다르게 구성
        //  여긴 컨트롤러 진입 전 단계라서 URL 경로 문자열로 대신 판단함)
        Object body;
        if (request.getRequestURI().startsWith("/api/auth")) {
            // auth 관련 API -> ApiResponse 형태로 응답
            body = ApiResponse.error("AUTH_INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다.");
        } else {
            // 그 외 -> 단순 message 형태로 응답
            body = Map.of("message", "인증이 필요합니다.");
        }

        // 구성한 객체를 실제 제이슨으로 변환해서 응답 스트림에 직접 씀
        objectMapper.writeValue(response.getWriter(), body);
    }
}