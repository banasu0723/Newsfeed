package org.sparta.newsfeed.domain.profiles.service;

import org.sparta.newsfeed.config.PasswordEncoder;
import org.sparta.newsfeed.domain.profiles.dto.PasswordUpdateRequestDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileResponseDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileUpdateRequestDto;
import org.sparta.newsfeed.domain.common.exception.CustomException;
import org.sparta.newsfeed.domain.common.exception.ExceptionMessage;
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
                .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND, id));

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
                .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND, id));

        // 새로운 이미지가 업로드되면 처리
        String profileImageName = null;
        if (updateDto.getProfileImage() != null && !updateDto.getProfileImage().isEmpty()) {
            profileImageName = saveProfileImage(updateDto.getProfileImage());
        }

        user.updateProfile(updateDto.getName(), updateDto.getIntroduction(), profileImageName);
        userRepository.save(user);

        // 프로필 이미지 경로 반환
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
                .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND, id));  // 사용자가 존재하지 않으면 예외 발생

        if (!passwordEncoder.matches(passwordUpdateDto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionMessage.INVALID_PASSWORD, id);
        }

        if (passwordUpdateDto.getPassword().equals(passwordUpdateDto.getNewPassword())) {
            throw new CustomException(ExceptionMessage.SAME_PASSWORD, id);
        }

        user.changePassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }

    // 프로필 이미지를 저장하고 파일 이름을 반환하는 메소드
    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String imageName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
        File imageFile = new File(IMAGE_DIR + imageName);
        imageFile.getParentFile().mkdirs();
        Files.write(Paths.get(imageFile.getAbsolutePath()), profileImage.getBytes());
        return imageName;
    }
}
