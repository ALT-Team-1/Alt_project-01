package com.alt.project.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
}
