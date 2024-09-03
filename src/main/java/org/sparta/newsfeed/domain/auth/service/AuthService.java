package org.sparta.newsfeed.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.config.JwtUtil;
import org.sparta.newsfeed.config.PasswordEncoder;
import org.sparta.newsfeed.domain.auth.dto.AuthUser;
import org.sparta.newsfeed.domain.auth.dto.request.SignDeleteRequestDto;
import org.sparta.newsfeed.domain.auth.dto.request.SigninRequestDto;
import org.sparta.newsfeed.domain.auth.dto.request.SignupRequestDto;
import org.sparta.newsfeed.domain.posts.profiles.exception.CustomException;
import org.sparta.newsfeed.domain.posts.profiles.exception.ExceptionMessage;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;

    }

    //회원가입
    @Transactional
    public String signUp(SignupRequestDto signupRequestDto) {

        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }

        // 비밀번호 조건
        // 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩
        String password = signupRequestDto.getPassword();
        //// 8글자 이상
        if(password.length()<8){
            log.error("비밀번호 8글자 미만");
            throw new IllegalArgumentException("비밀번호 8글자 미만");
        }
        //// 대문자 찾기
        boolean flag = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                flag = !flag;
                break;
            }
        }

        if (!flag) {
            log.error("비밀번호 대문자 미포함");
            throw new IllegalArgumentException("비밀번호 대문자 미포함");
        }
        //// 숫자 찾기
        flag = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                flag = !flag;
                break;
            }
        }
        if (!flag) {
            log.error("비밀번호 숫자 미포함");
            throw new IllegalArgumentException("비밀번호 숫자 미포함");
        }
        //// 특수문자 찾기
        flag = false;
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                flag = !flag;
                break;
            }
        }
        if (!flag) {
            log.error("비밀번호 특수문자 미포함");
            throw new IllegalArgumentException("비밀번호 특수문자 미포함");
        }


        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getUserName()
        );

        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(
                savedUser.getId(),
                savedUser.getEmail()
        );

        return bearerToken;
    }

    @Transactional
    public Void signDelete(AuthUser authUser, SignDeleteRequestDto signDeleteRequestDto) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new NullPointerException("잘못된 유저정보 입니다."));

        // 입력한 비밀번호 확인
        if(!passwordEncoder.matches(signDeleteRequestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        // 입력한 문구 확인
        String str1 = user.getEmail() + " 탈퇴";
        String str2 = signDeleteRequestDto.getCheckMessage();
        if (!str1.equals(str2)) {
            throw new IllegalArgumentException("잘못된 문구 입니다.");
        }

        user.unActivated(); //활정화 중지

        return null;
    }

    public String signIn(SigninRequestDto signinRequestDto) {
        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(()->new NullPointerException("잘못된 유저정보 입니다."));

        if (!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        if(!user.isActivate()){
            throw new IllegalArgumentException("이미 탈퇴한 사용자 입니다.");
        }

        String bearerToken = jwtUtil.createToken(
                user.getId(),
                user.getEmail()
        );

        return bearerToken;
    }
//
//    public Void signOut() {
//    }

    public AuthUser validateToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Claims claims = jwtUtil.extractClaims(token);

            Long userId = Long.parseLong(claims.getSubject());
            String email = claims.get("email", String.class);

            return new AuthUser(userId, email);
        } catch (IllegalArgumentException e) {
            log.error("토큰 파싱 오류: ", e);
            throw new CustomException(ExceptionMessage.INVALID_TOKEN, "토큰 파싱 오류");
        } catch (JwtException e) {
            log.error("JWT 오류: ", e);
            throw new CustomException(ExceptionMessage.INVALID_TOKEN, "JWT 오류");
        } catch (Exception e) {
            log.error("토큰 검증 실패: ", e);
            throw new CustomException(ExceptionMessage.INVALID_TOKEN, e.getMessage());
        }
    }
}
