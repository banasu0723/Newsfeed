package org.sparta.newsfeed.domain.profiles.controller;

import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.common.exception.ApplicationException;
import org.sparta.newsfeed.domain.profiles.dto.PasswordUpdateRequestDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileResponseDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileUpdateRequestDto;
import org.sparta.newsfeed.domain.common.exception.ErrorCode;
import org.sparta.newsfeed.domain.profiles.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/profiles")
@Validated
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    //프로필 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long id,
                                                         @RequestAttribute("userId") Long userId) {
        // 접근 권한이 없으면 예외 발생
        if (!userId.equals(id)) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_PROFILE_ACCESS);
        }

        ProfileResponseDto profile = profileService.getProfile(id);
        log.info("프로필 조회 성공 - ID: {}", id);
        return ResponseEntity.ok(profile);
    }

    //프로필 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @PathVariable Long id,
            @Valid @ModelAttribute ProfileUpdateRequestDto profileUpdateRequestDto,
            @RequestAttribute("userId") Long userId) throws IOException {
        // 접근 권한이 없으면 예외 발생
        if (!userId.equals(id)) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_PROFILE_ACCESS);
        }

        ProfileResponseDto updatedProfile = profileService.updateProfile(id, profileUpdateRequestDto);
        log.info("프로필 수정 성공 - ID: {}", id);
        return ResponseEntity.ok(updatedProfile);
    }

    //비밀번호 업데이트
    @PostMapping("/{id}/change-pwd")
    public ResponseEntity<String> changePassword(@PathVariable Long id,
                                                 @RequestBody @Valid PasswordUpdateRequestDto passwordUpdateDto,
                                                 @RequestAttribute("userId") Long userId) {
        // 접근 권한이 없으면 예외 발생
        if (!userId.equals(id)) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_PROFILE_ACCESS);
        }

        profileService.changePassword(id, passwordUpdateDto);
        log.info("비밀번호 변경 성공 - ID: {}", id);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
