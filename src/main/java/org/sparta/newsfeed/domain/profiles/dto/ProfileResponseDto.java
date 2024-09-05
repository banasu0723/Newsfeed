package org.sparta.newsfeed.domain.profiles.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String introduction;
    private final String profileImageUrl;
}
