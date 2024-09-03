package org.sparta.newsfeed.profiles.repository;

import org.sparta.newsfeed.profiles.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}
