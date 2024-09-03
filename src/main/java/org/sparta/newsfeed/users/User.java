package org.sparta.newsfeed.domain.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.newsfeed.domain.common.Timestamped;

@Getter
@Entity
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String image;
    private String introduction;
    private boolean activate;


    public User(String email, String password, String userName) {
        this.email = email;
        this.password=password;
        this.name=userName;
        this.activate = true;
    }


    public void unActivated() {
        this.activate = !this.activate;
    }
}