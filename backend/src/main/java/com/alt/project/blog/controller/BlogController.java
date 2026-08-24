package com.alt.project.blog.controller;

import com.alt.project.blog.dto.request.BlogRequest;
import com.alt.project.blog.dto.response.BlogResponse;
import com.alt.project.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blogs")
public class BlogController {
//
//    private final BlogService blogService;
//
//    @GetMapping
//    public ResponseEntity<List<BlogResponse>> getAllPosts() {
//        return ResponseEntity.ok(blogService.getAllPosts());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<BlogResponse> getPost(@PathVariable Long id) {
//        return ResponseEntity.ok(blogService.getPost(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<BlogResponse> createPost(
//            @Valid @RequestBody BlogRequest request,
//            Authentication authentication
//    ) {
//        BlogResponse response = blogService.createPost(request, authentication.getName());
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<BlogResponse> updatePost(
//            @PathVariable Long id,
//            @RequestBody BlogRequest request,
//            Authentication authentication
//    ) {
//        BlogResponse response = blogService.updatePost(id, request, authentication.getName());
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {
//        blogService.deletePost(id, authentication.getName());
//        return ResponseEntity.ok().build();
//    }
}
