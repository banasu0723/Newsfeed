package org.sparta.newsfeed.domain.likes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.comments.service.CommentService;
import org.sparta.newsfeed.domain.posts.entity.Post;
import org.sparta.newsfeed.domain.posts.service.PostService;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final CommentService commentService;
    private final PostService postService;
    private final UserRepository userRepository;


    // 뉴스피드에 좋아요 누르거나 취소하는 메서드
    @PostMapping("/{postId}")
    public ResponseEntity<String> addLikeAtPost(@PathVariable Long postId, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        User user = userRepository.findById(userId).get();

        String response = likeService.addLikeAtPost(postId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 뉴스피드의 댓글에 좋아요 누르거나 취소하는 메서드
    @PostMapping("/{postId}/{commentId}")
    public ResponseEntity<String> addLikeAtComment(@PathVariable Long postId, @PathVariable Long commentId, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        User user = userRepository.findById(userId).get();

        String response = likeService.addLikeAtComment(postId, commentId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
