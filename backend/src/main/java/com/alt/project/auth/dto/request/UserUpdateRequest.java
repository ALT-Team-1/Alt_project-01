package com.alt.project.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

public class UserUpdateRequest {
    private String nickname;
    private String password;
}
