package org.sparta.newsfeed.profiles.service;

import org.sparta.newsfeed.profiles.config.PasswordEncoder;
import org.sparta.newsfeed.profiles.dto.PasswordUpdateRequestDto;
import org.sparta.newsfeed.profiles.dto.ProfileResponseDto;
import org.sparta.newsfeed.profiles.dto.ProfileUpdateRequestDto;
import org.sparta.newsfeed.profiles.entity.User;
import org.sparta.newsfeed.profiles.exception.CustomException;
import org.sparta.newsfeed.profiles.exception.ExceptionMessage;
import org.sparta.newsfeed.profiles.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                null  // posts 추후 추가
        );
    }

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
                null  // posts 추후 추가
        );
    }

    @Transactional
    public void changePassword(Long id, PasswordUpdateRequestDto passwordUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND, id));

        if (!passwordEncoder.matches(passwordUpdateDto.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionMessage.INVALID_PASSWORD, id);
        }

        if (passwordUpdateDto.getPassword().equals(passwordUpdateDto.getNewPassword())) {
            throw new CustomException(ExceptionMessage.SAME_PASSWORD, id);
        }

        user.changePassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }
}
