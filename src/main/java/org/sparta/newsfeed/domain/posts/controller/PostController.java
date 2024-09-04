package org.sparta.newsfeed.domain.posts.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.posts.dto.PostRequestDto;
import org.sparta.newsfeed.domain.posts.dto.PostResponseDto;
import org.sparta.newsfeed.domain.posts.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final PostService postService;


    // 게시물 작성
    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");

        return postService.createPost(postRequestDto, userId);
    }


    // 친구 뉴스피드 조회
    @GetMapping("/newsfeeds")
    public ResponseEntity<List<PostResponseDto>> getFriendNewsfeeds(HttpServletRequest httpRequest, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("친구 뉴스피드 목록 조회 - 페이지 인덱스: {}, 페이지: {}, 사이즈: {}", pageable.getPageNumber(), pageable.getPageNumber() + 1, pageable.getPageSize());


        Long userId = (Long) httpRequest.getAttribute("userId");
        List<PostResponseDto> res = postService.getFriendNewsfeeds(userId, pageable);

        log.info("{} 페이지에서 조회된 뉴스피드 수: {} 개", pageable.getPageNumber() + 1, res.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }


    // 개인 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getMyPosts(HttpServletRequest httpRequest, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        List<PostResponseDto> res = postService.getMyPosts(userId, pageable);
        return ResponseEntity.ok(res);
    }

    // 게시물 수정
    @PutMapping("/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(postId, postRequestDto);
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

}