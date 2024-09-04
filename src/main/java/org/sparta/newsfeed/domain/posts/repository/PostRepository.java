package org.sparta.newsfeed.domain.posts.repository;

import org.sparta.newsfeed.domain.posts.entity.Post;
import org.sparta.newsfeed.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIn(List<User> users);

    Collection<Object> findByUser(User user);
}