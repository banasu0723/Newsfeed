package org.sparta.newsfeed.domain.friendship.repository;

import org.sparta.newsfeed.domain.friendship.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

}
