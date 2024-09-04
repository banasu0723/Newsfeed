package org.sparta.newsfeed.domain.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class PostResponseDto {
    private int code;
    private String message;
    private PostData data;

    @Data
    @AllArgsConstructor
    public static class PostData {
        private String title;
        private String content;
        private Timestamp createdAt;
        private Timestamp updatedAt;
    }
}
