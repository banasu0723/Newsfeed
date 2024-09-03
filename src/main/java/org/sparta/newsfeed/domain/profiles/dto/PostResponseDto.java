package org.sparta.newsfeed.domain.profiles.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private final String title;
    private final String contents;
}
