package org.sparta.newsfeed.domain.auth.controller;

import org.sparta.newsfeed.domain.auth.dto.request.SignupRequestDto;
import org.sparta.newsfeed.domain.auth.dto.response.SignupResponseDto;
import org.sparta.newsfeed.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/auth/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        return authService.signup(signupRequestDto);
    }


}
