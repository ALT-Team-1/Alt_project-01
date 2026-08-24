package com.alt.project.auth.service;

import com.alt.project.auth.dto.request.LoginRequest;
import com.alt.project.auth.dto.request.SignupRequest;
import com.alt.project.auth.dto.request.UserUpdateRequest;
import com.alt.project.auth.dto.response.SignupResponse;
import com.alt.project.auth.dto.response.TokenResponse;
import com.alt.project.auth.dto.response.UserResponse;
import com.alt.project.auth.entity.UserEntity;
import com.alt.project.global.jwt.JwtProvider;
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
        validateDuplicateEmail(request.getEmail());

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);

        String accessToken = jwtProvider.createToken(user.getEmail());
        return new SignupResponse(user.getId(), user.getNickname(), accessToken);
    }

    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(AuthException::invalidCredentials);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw AuthException.invalidCredentials();
        }

        String accessToken = jwtProvider.createToken(user.getEmail());
        return new TokenResponse(accessToken);
    }

    public UserResponse getMyInfo(String email) {
        UserEntity user = findUserOrThrow(email);
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public UserResponse updateMyInfo(String email, UserUpdateRequest request) {
        UserEntity user = findUserOrThrow(email);

        user.updateNickname(request.getNickname());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        }
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public void deleteMyAccount(String email) {
        UserEntity user = findUserOrThrow(email);
        userRepository.delete(user);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw AuthException.duplicateEmail();
        }
    }

    private UserEntity findUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(AuthException::invalidCredentials);
    }
}