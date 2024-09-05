package org.sparta.newsfeed.domain.profiles.service;

import org.sparta.newsfeed.config.PasswordEncoder;
import org.sparta.newsfeed.domain.common.exception.ApplicationException;
import org.sparta.newsfeed.domain.profiles.dto.PasswordUpdateRequestDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileResponseDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileUpdateRequestDto;
import org.sparta.newsfeed.domain.common.exception.ErrorCode;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String IMAGE_DIR = "src/main/resources/static/images/";

    // 프로필 조회
    @Transactional
    public ProfileResponseDto getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 프로필 이미지 경로를 반환
        String profileImageUrl = user.getProfileImage() != null ? "/images/" + user.getProfileImage() : null;

        return new ProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIntroduction(),
                profileImageUrl
        );
    }

    // 프로필 업데이트
    @Transactional
    public ProfileResponseDto updateProfile(Long id, ProfileUpdateRequestDto updateDto) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 기존 프로필 이미지 이름 저장
        String existingProfileImageName = user.getProfileImage();

        // 새로운 이미지가 업로드된 경우 처리
        String profileImageName = null;
        if (updateDto.getProfileImage() != null && !updateDto.getProfileImage().isEmpty()) {
            // 새로운 이미지를 저장하고 파일 이름을 가져옴
            profileImageName = saveProfileImage(updateDto.getProfileImage());

            // 기존 이미지가 있는 경우 삭제
            if (existingProfileImageName != null) {
                deleteProfileImage(existingProfileImageName);
            }
        } else if (updateDto.getProfileImage() == null && existingProfileImageName != null) {
            // 이미지가 null로 보내졌고, 기존 이미지가 있는 경우 삭제
            deleteProfileImage(existingProfileImageName);
            profileImageName = null; // 이미지가 null임을 명시
        }

        user.updateProfile(updateDto.getName(), updateDto.getIntroduction(), profileImageName);
        userRepository.save(user);

        // 프로필 이미지 URL 반환
        String profileImageUrl = profileImageName != null ? "/images/" + profileImageName : null;

        return new ProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIntroduction(),
                profileImageUrl
        );
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long id, PasswordUpdateRequestDto passwordUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 기존 비밀번호가 일치하지 않으면 예외 발생
        if (!passwordEncoder.matches(passwordUpdateDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호가 기존 비밀번호와 동일하면 예외 발생
        if (passwordUpdateDto.getPassword().equals(passwordUpdateDto.getNewPassword())) {
            throw new ApplicationException(ErrorCode.SAME_PASSWORD);
        }

        // 비밀번호 변경 및 저장
        user.changePassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }

    // 프로필 이미지를 저장
    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String imageName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
        File imageFile = new File(IMAGE_DIR + imageName);
        imageFile.getParentFile().mkdirs(); // 디렉토리 없을때 이미지 저장 경로 생성
        Files.write(Paths.get(imageFile.getAbsolutePath()), profileImage.getBytes());
        return imageName;
    }

    // 프로필 이미지 삭제
    private void deleteProfileImage(String imageName) {
        File file = new File(IMAGE_DIR + imageName);
        if (file.exists()) {
            if (!file.delete()) {
                throw new ApplicationException(ErrorCode.GENERAL_EXCEPTION);
            }
        }
    }
}
