package org.sparta.newsfeed.domain.comments.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.domain.auth.dto.AuthUser;
import org.sparta.newsfeed.domain.comments.dto.CommentRequestDto;
import org.sparta.newsfeed.domain.comments.dto.CommentResponseDto;
import org.sparta.newsfeed.domain.comments.entity.Comment;
import org.sparta.newsfeed.domain.comments.repository.CommentRepository;
import org.sparta.newsfeed.domain.posts.entity.Post;
import org.sparta.newsfeed.domain.posts.repository.PostRepository;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.sparta.newsfeed.domain.friendship.repository.FriendshipRepository;
import org.sparta.newsfeed.domain.common.exception.ApplicationException;
import org.sparta.newsfeed.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long postId, AuthUser authUser, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 친구 관계가 아니면 댓글 작성 제한, 본인의 게시글에는 작성 가능
        if (!isFriend(user.getId(), post.getUser().getId()) && !post.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_COMMENT_ACCESS);
        }

        Comment comment = new Comment(post, user, requestDto.getBody());
        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getId(),
                post.getId(),
                comment.getBody(),
                user.getName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    // 댓글 조회 (친구 관계만 댓글 조회 가능)
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long postId, AuthUser authUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
        User currentUser = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 본인 게시글 또는 친구 관계인 경우만 댓글 조회 가능
        if (!isFriend(currentUser.getId(), post.getUser().getId()) && !post.getUser().getId().equals(currentUser.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_COMMENT_ACCESS);
        }

        // Fetch Join을 통해 Post와 User 정보를 미리 로딩
        List<Comment> comments = commentRepository.findAllByPostWithUserAndPost(post);

        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        post.getId(),
                        comment.getBody(),
                        comment.getUser().getName(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, AuthUser authUser, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(authUser.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_COMMENT_ACCESS);
        }

        comment.update(requestDto.getBody());
        return new CommentResponseDto(
                comment.getId(),
                post.getId(),
                comment.getBody(),
                comment.getUser().getName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long commentId, AuthUser authUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(authUser.getId())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_COMMENT_ACCESS);
        }

        commentRepository.delete(comment);
    }

    // 친구 관계 확인 메서드
    private boolean isFriend(Long currentUserId, Long postOwnerId) {
        return friendshipRepository.findByUserIdAndFriendId(currentUserId, postOwnerId).isPresent() ||
                friendshipRepository.findByUserIdAndFriendId(postOwnerId, currentUserId).isPresent();
    }
}
