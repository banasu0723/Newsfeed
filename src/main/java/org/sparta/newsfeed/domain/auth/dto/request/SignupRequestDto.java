package org.sparta.newsfeed.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    String userName;
    String password;
    String email;
}
