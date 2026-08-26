package com.alt.project.blog.service;

import com.alt.project.auth.entity.UserEntity;
import com.alt.project.auth.repository.UserRepository;
import com.alt.project.blog.dto.request.BlogRequest;
import com.alt.project.blog.dto.response.BlogResponse;
import com.alt.project.blog.entity.BlogEntity;
import com.alt.project.blog.repository.BlogRepository;
import com.alt.project.global.exception.AuthException;
import com.alt.project.global.exception.BlogException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    // 전체 게시글 목록 조회
    public List<BlogResponse> getAllPosts() {
        return blogRepository.findAll().stream()
                .map(BlogResponse::from)
                .toList();
    }

    // 게시글 단일 조회
    public BlogResponse getPost(Long id) {
        BlogEntity post = findPostOrThrow(id);
        return BlogResponse.from(post);
    }

    // 게시글 작성
    @Transactional
    public BlogResponse createPost(BlogRequest request, String email) {
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(AuthException::invalidCredentials);

        BlogEntity post = BlogEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        blogRepository.save(post);
        return BlogResponse.from(post);
    }

    // 게시글 수정
    @Transactional
    public BlogResponse updatePost(Long id, BlogRequest request, String email) {
        BlogEntity post = findPostOrThrow(id);

        if (!post.isWrittenBy(email)) {
            throw BlogException.forbiddenUpdate();
        }

        post.update(request.getTitle(), request.getContent());
        return BlogResponse.from(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id, String email) {
        BlogEntity post = findPostOrThrow(id);

        if (!post.isWrittenBy(email)) {
            throw BlogException.forbiddenDelete();
        }

        blogRepository.delete(post);
    }

    // 엔터티 테이블에서 id값을 통해 게시글 조회하고 없으면 던짐
    private BlogEntity findPostOrThrow(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(BlogException::notFound);
    }
}
