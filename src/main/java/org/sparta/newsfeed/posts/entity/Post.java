package org.sparta.newsfeed.posts.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User id;
    */

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public Post(String title, String content, Timestamp createdAt, Timestamp updatedAt) {
        //this.user = user;
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
}
