package org.sparta.newsfeed.domain.friendship.repository;

import org.sparta.newsfeed.domain.friendship.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByUserIdAndFriendId(Long friendId, Long id);
}
