package org.sparta.newsfeed.domain.auth.service;

import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
