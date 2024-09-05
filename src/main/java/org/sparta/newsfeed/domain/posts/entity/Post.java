package org.sparta.newsfeed.domain.posts.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.newsfeed.domain.users.entity.User;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Integer likeCount =0; // 좋아요 수

    @Builder
    public Post(String title, String content, Timestamp createdAt, Timestamp updatedAt, User user) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 게시물 수정 시 타임스탬프 업데이트
    public Post update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        return this;
    }

    // 좋아요 설정
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }


}
