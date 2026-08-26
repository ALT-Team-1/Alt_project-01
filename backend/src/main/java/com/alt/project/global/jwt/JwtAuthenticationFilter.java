package com.alt.project.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// 모든 HTTP 요청에서 jwt를 검사해 누가 보낸 요청인지를 스프링 시큐리티에 등록하는 필터
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider; // 토큰 검증

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 헤더에서 토큰만 뽑음
        String token = resolveToken(request);

        // 토큰이 있고 유효한 토큰이면 인증 처리 진행
        if (token != null && jwtProvider.validateToken(token)) {

            String email = jwtProvider.getEmail(token); // 토큰 안에서 이메일추출

            // 누가 이 요청을 보냈는지 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,                    // 인증된 사용자
                            null,                      // 비밀번호는 이미 검증 끝났으니 불필요
                            Collections.emptyList()    // 권한 목록 - 역할 구분이 없어 빈 리스트
                    );

            // 컨트롤러의 Authentication 파라미터로 이 정보가 그대로 전달됨
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 토큰이 없거나 유효하지 않아도 일단 다음 단계로 넘김
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer "가 7글자이므로 7번째 글자부터 끝까지가 실제 토큰
        }
        return null;
    }
}