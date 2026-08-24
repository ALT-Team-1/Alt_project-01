package com.alt.project.auth.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

public class UserUpdateRequest {

    private String nickname;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;
}
