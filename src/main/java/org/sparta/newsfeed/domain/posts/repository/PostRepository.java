package org.sparta.newsfeed.domain.posts.repository;

import org.sparta.newsfeed.domain.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}