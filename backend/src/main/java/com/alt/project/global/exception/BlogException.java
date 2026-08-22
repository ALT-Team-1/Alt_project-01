package com.alt.project.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BlogException extends RuntimeException {

    private final HttpStatus status;

    private BlogException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static BlogException notFound() {
        return new BlogException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
    }

    public static BlogException forbiddenUpdate() {
        return new BlogException(HttpStatus.FORBIDDEN, "본인이 작성한 게시물만 수정할 수 있습니다.");
    }

    public static BlogException forbiddenDelete() {
        return new BlogException(HttpStatus.FORBIDDEN, "본인이 작성한 게시물만 삭제할 수 있습니다.");
    }
}