package com.alt.project.blog.dto.response;

import com.alt.project.blog.entity.BlogEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private String author;

    public static BlogResponse from(BlogEntity entity) {
        return new BlogResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthor().getNickname()
        );
    }
}