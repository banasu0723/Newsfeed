package org.sparta.newsfeed.posts.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.posts.dto.PostRequestDto;
import org.sparta.newsfeed.posts.dto.PostResponseDto;
import org.sparta.newsfeed.posts.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }
}
