package org.sparta.newsfeed.profiles.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String image;

    @Column(length = 255)
    private String introduction;

    @Column(nullable = false)
    private boolean activate;

    // 도메인 메서드를 통해 프로필 수정
    public void updateProfile(String name, String introduction, String profileImage) {
        this.name = name;
        this.image = profileImage;
        this.introduction = introduction;
        this.modifiedAt = LocalDateTime.now(); // 수정 시간 갱신
    }

    // 비밀번호 수정
    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.modifiedAt = LocalDateTime.now(); // 수정 시간 갱신
    }
}
