package org.sparta.newsfeed.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final String bearerToken;

    public SignupResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
