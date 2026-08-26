package com.alt.project.auth.service;

import com.alt.project.auth.dto.request.LoginRequest;
import com.alt.project.auth.dto.request.SignupRequest;
import com.alt.project.auth.dto.request.UserUpdateRequest;
import com.alt.project.auth.dto.response.SignupResponse;
import com.alt.project.auth.dto.response.TokenResponse;
import com.alt.project.auth.dto.response.UserResponse;
import com.alt.project.auth.entity.UserEntity;
import com.alt.project.auth.jwt.JwtProvider;
import com.alt.project.auth.repository.UserRepository;
import com.alt.project.global.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        validateDuplicateEmail(request.getEmail()); // 이메일 중복 시 예외처리

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // DB 비밀번호 암호화
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);

        // 가입 완료 시 토큰 발급
        String accessToken = jwtProvider.createToken(user.getEmail());
        return new SignupResponse(user.getId(), user.getNickname(), accessToken);
    }

    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail()) // 이메일 존재 여부 검사
                .orElseThrow(AuthException::invalidCredentials);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw AuthException.invalidCredentials(); // 비밀번호 일치 판단
        }

        String accessToken = jwtProvider.createToken(user.getEmail());
        return new TokenResponse(accessToken); // 토큰 생성 후 발급
    }

    // 이메일로 유저 조회
    public UserResponse getMyInfo(String email) {
        UserEntity user = findUserOrThrow(email);
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public UserResponse updateMyInfo(String email, UserUpdateRequest request) {
        UserEntity user = findUserOrThrow(email); // 이메일로 DB에 유저를 조회해 존재하지 않으면 던짐

        // 닉네임 변경과 비밀번호가 널빈값이 아니면 암호화해서 변경
        user.updateNickname(request.getNickname());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        }
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname()); // DTO 반환
    }


    @Transactional

    // DB에서 유저 삭제
    public void deleteMyAccount(String email) {
        UserEntity user = findUserOrThrow(email);
        userRepository.delete(user);
    }

    // 이메일 중복 검사 (이미 이메일이 존재하면 던짐)
    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw AuthException.duplicateEmail();
        }
    }

    // 이메일로 유저를 조회 (해당 이메일이 없으면 던짐)
    private UserEntity findUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(AuthException::invalidCredentials);
    }
}