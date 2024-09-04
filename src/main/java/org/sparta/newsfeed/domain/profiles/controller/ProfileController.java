package org.sparta.newsfeed.domain.profiles.controller;

import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.profiles.dto.PasswordUpdateRequestDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileResponseDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileUpdateRequestDto;
import org.sparta.newsfeed.domain.profiles.exception.CustomException;
import org.sparta.newsfeed.domain.profiles.exception.ExceptionMessage;
import org.sparta.newsfeed.domain.profiles.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
        if (!userId.equals(id)) {
            throw new CustomException(ExceptionMessage.FORBIDDEN_PROFILE_ACCESS);
        }

        ProfileResponseDto profile = profileService.getProfile(id);
        log.info("프로필 조회 성공 - ID: {}", id);
        return ResponseEntity.ok(profile);
    }

    //프로필 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> updateProfile(@PathVariable Long id,
                                                            @RequestBody @Valid ProfileUpdateRequestDto updateDto,
                                                            @RequestAttribute("userId") Long userId) {
        if (!userId.equals(id)) {
            throw new CustomException(ExceptionMessage.FORBIDDEN_PROFILE_ACCESS);
        }

        ProfileResponseDto updatedProfile = profileService.updateProfile(id, updateDto);
        log.info("프로필 수정 성공 - ID: {}", id);
        return ResponseEntity.ok(updatedProfile);
    }

    //비밀번호 업데이트
    @PostMapping("/{id}/change-pwd")
    public ResponseEntity<String> changePassword(@PathVariable Long id,
                                                 @RequestBody @Valid PasswordUpdateRequestDto passwordUpdateDto,
                                                 @RequestAttribute("userId") Long userId) {
        if (!userId.equals(id)) {
            throw new CustomException(ExceptionMessage.FORBIDDEN_PROFILE_ACCESS);
        }

        profileService.changePassword(id, passwordUpdateDto);
        log.info("비밀번호 변경 성공 - ID: {}", id);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
