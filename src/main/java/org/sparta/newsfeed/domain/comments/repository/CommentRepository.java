package org.sparta.newsfeed.domain.comments.repository;

import org.sparta.newsfeed.domain.comments.entity.Comment;
import org.sparta.newsfeed.domain.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.post WHERE c.post = :post")
    List<Comment> findAllByPostWithUserAndPost(Post post);
}
