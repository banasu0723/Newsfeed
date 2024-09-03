package org.sparta.newsfeed.domain.profiles.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileUpdateRequestDto {

    @NotBlank(message = "이름은 비워둘 수 없습니다.")
    private final String name;

    private final String introduction;

    private final String profileImage;
}
