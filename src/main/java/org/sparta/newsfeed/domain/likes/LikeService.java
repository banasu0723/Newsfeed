package org.sparta.newsfeed.domain.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.comments.entity.Comment;
import org.sparta.newsfeed.domain.comments.repository.CommentRepository;
import org.sparta.newsfeed.domain.posts.entity.Post;
import org.sparta.newsfeed.domain.posts.repository.PostRepository;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    // 뉴스피드에 좋아요 누르고 취소하는 메서드(개추 ㅋ)
    public String addLikeAtPost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시물입니다."));

        if (post.getUser().getId() == user.getId()) {
            throw new IllegalArgumentException("본인이 남긴 게시글에는 '좋아요'를 누를 수 없습니다");
        }

        // 좋아요 개수 초기화(Null 방지)
        Integer existingLikeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
        post.setLikeCount(existingLikeCount);

        if (!likeRepository.existsByUserAndPost(user, post)) { // 사용자가 이미 이 게시물에 좋아요를 눌렀는지 확인

            post.setLikeCount(post.getLikeCount() + 1); // 좋아요가 없으면 좋아요 추가
            Like likePost = Like.builder()
                    .post(post)
                    .user(user)
                    .build();

            likeRepository.save(likePost); // 좋아요 저장

            return "해당 뉴스피드에 '좋아요'를 눌렀습니다";

        } else {  // 좋아요가 있으면 좋아요 제거
            post.setLikeCount(post.getLikeCount() - 1);
            likeRepository.deleteByUserAndPost(user, post);

            return "해당 뉴스피드에 '좋아요'를 취소하였습니다";
        }
    }


    // 뉴스피드 댓글에 좋아요 누르고 취소하는 메서드
    public String addLikeAtComment(Long postId, Long commentId, User user) {

        Post post = postRepository.findById(postId).get();

        Comment comment = likeRepository.findByPostIdAndCommentId(postId, commentId);

        if (post.getUser().getId() == user.getId() || comment.getUser().getId() == user.getId()) {
            throw new IllegalArgumentException("본인이 남긴 게시글에는 '좋아요'를 누를 수 없습니다");
        }

        // 좋아요 개수 초기화(Null 방지)
        Integer existingLikeCount = comment.getLikeCount() != null ? comment.getLikeCount() : 0;
        comment.setLikeCount(existingLikeCount);

        if (!likeRepository.existsByUserAndComment(user, comment)) { // 사용자가 이미 이 댓글에 좋아요를 눌렀는지 확인

            comment.setLikeCount(comment.getLikeCount() + 1); // 좋아요가 없으면 좋아요 추가
            Like likeComment = Like.builder()
                    .comment(comment)
                    .post(comment.getPost())
                    .user(user)
                    .build();

            likeRepository.save(likeComment); // 좋아요 저장

            return "해당 댓글에 '좋아요'를 눌렀습니다";

        } else {  // 좋아요가 있으면 좋아요 제거
            comment.setLikeCount(comment.getLikeCount() - 1);
            likeRepository.deleteByUserAndComment(user, comment);

            return "해당 댓글에 '좋아요'를 취소하였습니다";

        }
    }

}
