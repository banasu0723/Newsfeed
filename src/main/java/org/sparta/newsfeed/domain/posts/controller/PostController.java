package org.sparta.newsfeed.domain.posts.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.domain.posts.dto.PostRequestDto;
import org.sparta.newsfeed.domain.posts.dto.PostResponseDto;
import org.sparta.newsfeed.domain.posts.service.PostService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }


}