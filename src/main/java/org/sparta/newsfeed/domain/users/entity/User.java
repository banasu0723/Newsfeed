package org.sparta.newsfeed.domain.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.newsfeed.domain.common.Timestamped;
import org.sparta.newsfeed.domain.friendship.entity.Friendship;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
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

    // 친구 리스트
    @OneToMany(mappedBy = "user")
    private List<Friendship> friendshipUserList = new ArrayList<>();


    public User(String email, String password, String userName) {
        this.email = email;
        this.password=password;
        this.name=userName;
        this.activate = true;
    }

    public void updateProfile(String name, String introduction, String profileImage) {
        this.name = name;
        this.image = profileImage;
        this.introduction = introduction;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void unActivated() {
        this.activate = !this.activate;
    }
}
