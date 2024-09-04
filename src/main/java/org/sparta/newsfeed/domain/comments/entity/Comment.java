package org.sparta.newsfeed.domain.comments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.newsfeed.domain.posts.entity.Post;
import org.sparta.newsfeed.domain.users.entity.User;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String body;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public Comment(Post post, User user, String body) {
        this.post = post;
        this.user = user;
        this.body = body;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public void update(String body) {
        this.body = body;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
