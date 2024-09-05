package org.sparta.newsfeed.domain.profiles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordUpdateRequestDto {

    @NotBlank(message = "현재 비밀번호는 비워둘 수 없습니다.")
    private final String password;

    @NotBlank(message = "새 비밀번호는 비워둘 수 없습니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "새 비밀번호는 최소 8자 이상이어야 하며, 영문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다."
    )
    private final String newPassword;
}
