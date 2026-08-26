package com.alt.project.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    // 야믈 파일의 jwt.secret 값을 그대로 주입 (토큰 서명에 쓸 비밀 키 원본 문자열)
    @Value("${jwt.secret}")
    private String secretKey;

    // 야믈 파일의 jwt.access-token-validity 값 또한 그대로 주입 (토큰 유효기간)
    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    // 시크릿 키를 jwt 라이브러리가 실제 검증에 쓸 수 있는 Key 객체로 변환
    private Key key;

    // 스프링이 이 빈을 생성하고 @Value 주입까지 끝낸 직후 딱 한 번 자동 실행됨
    // (문자열 -> Key 변환을 매번 하지 않고 여기서 미리 한 번만 해둠)
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 로그인/회원가입 성공 시 호출 - email을 기반으로 새 JWT 토큰 문자열을 생성해 반환
    public String createToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidity); // 지금 시각 + 유효기간 = 만료 시각

        return Jwts.builder()
                .setSubject(email)                           // 토큰 주인
                .setIssuedAt(now)                            // 발급 시각
                .setExpiration(expiry)                       // 만료 시각
                .signWith(key, SignatureAlgorithm.HS256)     // key로 서명해 위조 불가능
                .compact();
    }

    // 토큰을 해석해서 저장된 이메일을 꺼냄
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)   // 서명 검증 + 파싱을 동시에 수행 (위조 토큰이면 여기서 예외 발생)
                .getBody()
                .getSubject();
    }

    // 토큰이 유효한 지 검사한 후 단순화해 반환
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true; // 여기까지 예외 없이 왔으면 정상 토큰
        } catch (ExpiredJwtException e) {
            // 유효기간이 지난 토큰
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException: 서명 위조나 형식 오류 등등 모든 JWT 문제의 총합
            // IllegalArgumentException: 토큰 문자열이 null이거나 비어있는 경우
            return false;
        }
    }
}