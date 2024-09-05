package org.sparta.newsfeed.domain.posts.repository;

import org.sparta.newsfeed.domain.posts.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자 ID 리스트에 해당하는 게시물들을 가져옴
    List<Post> findByUserIdIn(List<Long> userIds, Pageable pageable);

    // 유저 ID로 해당 유저의 모든 게시글 반환
    List<Post> findByUserId(Long userId, Pageable pageable);
}