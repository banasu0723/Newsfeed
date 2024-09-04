package org.sparta.newsfeed.domain.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.friendship.entity.Friendship;

import java.time.LocalDateTime;

@Getter
@Slf4j
@AllArgsConstructor
public class FriendshipResponseDto {

    private Long friendId;
    private String friendEmail;
    private LocalDateTime createdAt;

    public static FriendshipResponseDto toFriendResponseDto(Friendship friendship) {
        return new FriendshipResponseDto(
                friendship.getFriendId(),
                friendship.getFriendEmail(),
                friendship.getCreatedAt());
    }
}
