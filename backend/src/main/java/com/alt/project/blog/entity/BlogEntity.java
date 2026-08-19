package com.alt.project.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@AllArgsConstructor
@Getter
@Builder

public class BlogEntity {
    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
}
