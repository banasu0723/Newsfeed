package org.sparta.newsfeed.profiles.controller;

import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.profiles.dto.*;
import org.sparta.newsfeed.profiles.exception.CustomException;
import org.sparta.newsfeed.profiles.exception.ExceptionMessage;
import org.sparta.newsfeed.profiles.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/profiles")
@Validated
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    private boolean isTokenValid(String token) {
        boolean isValid = token != null && token.equals("Bearer dummy-token");
        log.debug("토큰 유효성 검사: {}, 결과: {}", token, isValid);
        return isValid;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id,
                                        @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isTokenValid(token)) {
            throw new CustomException(ExceptionMessage.INVALID_TOKEN, token);  // token 전달
        }

        ProfileResponseDto profile = profileService.getProfile(id);
        log.info("프로필 조회 성공 - ID: {}", id);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                           @RequestBody @Valid ProfileUpdateRequestDto updateDto,
                                           @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isTokenValid(token)) {
            throw new CustomException(ExceptionMessage.INVALID_TOKEN, token);  // token 전달
        }

        ProfileResponseDto updatedProfile = profileService.updateProfile(id, updateDto);
        log.info("프로필 수정 성공 - ID: {}", id);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/{id}/change-pwd")
    public ResponseEntity<String> changePassword(@PathVariable Long id,
                                                 @RequestBody @Valid PasswordUpdateRequestDto passwordUpdateDto,
                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        if (!isTokenValid(token)) {
            throw new CustomException(ExceptionMessage.INVALID_TOKEN, token);  // token 전달
        }

        profileService.changePassword(id, passwordUpdateDto);
        log.info("비밀번호 변경 성공 - ID: {}", id);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
