package org.sparta.newsfeed.domain.profiles.service;

import org.sparta.newsfeed.config.PasswordEncoder;
import org.sparta.newsfeed.domain.profiles.dto.PasswordUpdateRequestDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileResponseDto;
import org.sparta.newsfeed.domain.profiles.dto.ProfileUpdateRequestDto;
import org.sparta.newsfeed.domain.profiles.exception.CustomException;
import org.sparta.newsfeed.domain.profiles.exception.ExceptionMessage;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //프로필 조회
    @Transactional
    public ProfileResponseDto getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND, id));

        return new ProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIntroduction(),
                user.getImage(),
                null  //게시물 추가 예정
        );
    }

    //프로필 업데이트
    @Transactional
    public ProfileResponseDto updateProfile(Long id, ProfileUpdateRequestDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND, id));

        user.updateProfile(updateDto.getName(), updateDto.getIntroduction(), updateDto.getProfileImage());
        userRepository.save(user);

        return new ProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIntroduction(),
                user.getImage(),
                null  // 추후 게시물 추가 예정
        );
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long id, PasswordUpdateRequestDto passwordUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND, id));  // 사용자가 존재하지 않으면 예외 발생

        // 현재 비밀번호가 일치하지 않는 경우 예외 발생
        if (!passwordEncoder.matches(passwordUpdateDto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionMessage.INVALID_PASSWORD, id);
        }

        // 새 비밀번호가 현재 비밀번호와 동일한 경우 예외 발생
        if (passwordUpdateDto.getPassword().equals(passwordUpdateDto.getNewPassword())) {
            throw new CustomException(ExceptionMessage.SAME_PASSWORD, id);
        }

        // 새 비밀번호로 변경
        user.changePassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }
}


//test
