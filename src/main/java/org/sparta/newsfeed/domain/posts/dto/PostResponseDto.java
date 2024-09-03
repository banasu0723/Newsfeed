package org.sparta.newsfeed.domain.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Data
@Getter
@AllArgsConstructor
public class PostResponseDto {
    private int code;
    private String message;
    private PostData data;

    @Data
    @Getter
    @AllArgsConstructor
    public static class PostData {
        private String title;
        private String content;
        private Timestamp createdAt;
        private Timestamp updatedAt;
    }
}
