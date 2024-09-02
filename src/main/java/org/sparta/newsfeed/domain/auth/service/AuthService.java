package org.sparta.newsfeed.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.sparta.newsfeed.config.JwtUtil;
import org.sparta.newsfeed.config.PasswordEncoder;
import org.sparta.newsfeed.domain.auth.dto.request.SignupRequestDto;
import org.sparta.newsfeed.domain.auth.dto.response.SignupResponseDto;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final HttpServletResponse httpServletResponse;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, HttpServletResponse httpServletResponse) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.httpServletResponse = httpServletResponse;
    }

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        User newUser = new User(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getUserName()
        );

        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );

        httpServletResponse.setHeader("Authorization",bearerToken);

        return new SignupResponseDto(bearerToken);
    }
}
