package org.sparta.newsfeed.domain.users.repository;

import org.sparta.newsfeed.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
