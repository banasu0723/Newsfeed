package org.sparta.newsfeed.domain.likes;

import org.sparta.newsfeed.domain.comments.entity.Comment;
import org.sparta.newsfeed.domain.friendship.entity.Friendship;
import org.sparta.newsfeed.domain.posts.entity.Post;
import org.sparta.newsfeed.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    /*
    테이블에서 한 로우라도 user_id와 post_id가 각 파라미터와 같은 값이 동시에 존재하는지 여부 검사
    SELECT EXISTS (
    SELECT 1
    FROM BoardMember
    WHERE member_id = ? AND board_id = ?
    );
     */
    boolean existsByUserAndPost(User user, Post post);

    // 테이블에서 한 로우라도 user_id와 post_id가 각 파라미터와 같은 값이 동시에 존재하는지 여부 검사
    boolean existsByUserAndComment(User user, Comment comment);

    /*
    특정 사용자가 특정 게시물에 대해 남긴 좋아요(Like) 기록을 삭제
    DELETE FROM like WHERE user_id = ? AND post_id = ?;
     */
    void deleteByUserAndPost(User user, Post post);

    // 특정 사용자가 특정 댓글에 대해 남긴 좋아요(Like) 기록을 삭제
    void deleteByUserAndComment(User user, Comment comment);


    // 특정 뉴스피드의 특정 댓글 찾아서 반환
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.id = :commentId")
    Comment findByPostIdAndCommentId(@Param("postId") Long postId, @Param("commentId") Long commentId);

}
