package org.sparta.newsfeed.domain.posts.profiles.controller;

import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.posts.profiles.dto.PasswordUpdateRequestDto;
import org.sparta.newsfeed.domain.posts.profiles.dto.ProfileResponseDto;
import org.sparta.newsfeed.domain.posts.profiles.dto.ProfileUpdateRequestDto;
import org.sparta.newsfeed.domain.posts.profiles.service.ProfileService;
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
        // 요청한 ID와 사용자 ID가 일치하지 않는 경우, 접근 권한 오류
        if (!userId.equals(id)) {
            log.error("사용자 ID 불일치: 토큰 ID = {}, 요청된 ID = {}", userId, id);
            return ResponseEntity.status(403).body(null);
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
        // 요청한 ID와 사용자 ID가 일치하지 않는 경우
        if (!userId.equals(id)) {
            log.error("사용자 ID 불일치: 토큰 ID = {}, 요청된 ID = {}", userId, id);
            return ResponseEntity.status(403).body(null); // 403 Forbidden
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
        // 요청한 ID와 사용자 ID가 일치하지 않는 경우
        if (!userId.equals(id)) {
            log.error("사용자 ID 불일치: 토큰 ID = {}, 요청된 ID = {}", userId, id);
            return ResponseEntity.status(403).body("비밀번호 변경 권한이 없습니다.");
        }

        profileService.changePassword(id, passwordUpdateDto);
        log.info("비밀번호 변경 성공 - ID: {}", id);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
