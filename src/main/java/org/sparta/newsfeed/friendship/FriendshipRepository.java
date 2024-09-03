package org.sparta.newsfeed.friendship;
import org.sparta.newsfeed.users.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<User> findByEmail(String email);
}
