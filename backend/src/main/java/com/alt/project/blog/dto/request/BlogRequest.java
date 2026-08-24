package com.alt.project.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

public class BlogRequest {

    @NotBlank(message = "제목 작성은 필수입니다.")
    private String title;

    @NotBlank(message = "내용 작성은 필수입니다.")
    private String content;
}
