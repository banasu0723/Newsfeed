package org.sparta.newsfeed.domain.comments.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.annotation.Auth;
import org.sparta.newsfeed.domain.auth.dto.AuthUser;
import org.sparta.newsfeed.domain.comments.dto.CommentRequestDto;
import org.sparta.newsfeed.domain.comments.dto.CommentResponseDto;
import org.sparta.newsfeed.domain.comments.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @Auth AuthUser authUser,
            @RequestBody CommentRequestDto requestDto
    ) {
        CommentResponseDto response = commentService.createComment(postId, authUser, requestDto);
        return ResponseEntity.ok(response);
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable Long postId,
            @Auth AuthUser authUser  // AuthUser 추가
    ) {
        List<CommentResponseDto> comments = commentService.getComments(postId, authUser);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Auth AuthUser authUser,
            @RequestBody CommentRequestDto requestDto
    ) {
        CommentResponseDto response = commentService.updateComment(postId, commentId, authUser, requestDto);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Auth AuthUser authUser
    ) {
        commentService.deleteComment(postId, commentId, authUser);
        return ResponseEntity.noContent().build();
    }
}
