package org.sparta.newsfeed.domain.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private String body;
    private String author;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
