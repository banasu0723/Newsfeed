package org.sparta.newsfeed.domain.posts.profiles.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String introduction;
    private final String profileImage;
    private final List<PostResponseDto> posts;
}
