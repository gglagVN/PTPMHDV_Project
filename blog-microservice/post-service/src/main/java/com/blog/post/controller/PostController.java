package com.blog.post.controller;

import com.blog.post.dto.PostDto;
import com.blog.post.entity.Post;
import com.blog.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    // 1. Tạo bài viết mới
    @PostMapping
    public Post createPost(@RequestBody PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setAuthorId(postDto.getAuthorId()); // Tạm thời lấy từ body request

        return postRepository.save(post);
    }

    // 2. Lấy danh sách bài viết
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 3. Lấy chi tiết bài viết
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }
}