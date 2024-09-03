package org.sparta.newsfeed.domain.posts.repository;

import Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
