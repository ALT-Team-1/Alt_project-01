package com.alt.project.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class SignupResponse {
    private Long userId;
    private String nickname;
    private String accessToken;
}
