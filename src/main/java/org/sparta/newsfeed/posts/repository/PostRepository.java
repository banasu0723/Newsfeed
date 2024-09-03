package org.sparta.newsfeed.posts.repository;

import org.sparta.newsfeed.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
