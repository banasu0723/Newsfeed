package org.sparta.newsfeed.domain.auth.controller;

import org.sparta.newsfeed.annotation.Auth;
import org.sparta.newsfeed.domain.auth.dto.AuthUser;
import org.sparta.newsfeed.domain.auth.dto.request.SignDeleteRequestDto;
import org.sparta.newsfeed.domain.auth.dto.request.SigninRequestDto;
import org.sparta.newsfeed.domain.auth.dto.request.SignupRequestDto;
import org.sparta.newsfeed.domain.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto signupRequestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION,authService.signUp(signupRequestDto))
                .body("회원 가입 완료");
    }

    @PostMapping("/auth/signdelete")
    public ResponseEntity<String> singDelete(@Auth AuthUser authUser, @RequestBody SignDeleteRequestDto signDeleteRequestDto){
        authService.signDelete(authUser, signDeleteRequestDto);

        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<String> signIn(@RequestBody SigninRequestDto signinRequestDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION,authService.signIn(signinRequestDto))
                .body("로그인 완료");
    }
//
//    @PostMapping("/auth/signout")
//    public ResponseEntity<Void> singOut(){
//        return ResponseEntity.ok(authService.signOut());
//    }


}
