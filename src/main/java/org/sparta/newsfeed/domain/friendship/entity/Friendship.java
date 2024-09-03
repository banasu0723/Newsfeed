package org.sparta.newsfeed.domain.friendship.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sparta.newsfeed.domain.friendship.FriendshipRequestStatus;
import org.sparta.newsfeed.domain.friendship.FriendshipStatus;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.common.Timestamped;

import java.time.LocalDateTime;

/**
 * user: 본인 , friend : 요청 받은 상대방 또는 요청한 상대방
 */
@Entity
@Table(name = "friendship")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(name = "friendship_status", nullable = false)
    private FriendshipStatus friendshipStatus;

    private String myEmail;
    private String friendEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private FriendshipRequestStatus requestStatus; // 요청을 받았는지 보냈는지 구분

    @Column(name = "friend_id", nullable = false)
    private Long friendId;

    @Builder
    public Friendship(Long id, User user, User friend, FriendshipStatus friendshipStatus, String myEmail, String friendEmail, FriendshipRequestStatus requestStatus, LocalDateTime modifiedAt, Long friendId) {
        this.id = id;
        this.user = user;
        this.friendshipStatus = friendshipStatus;
        this.myEmail = myEmail;
        this.friendEmail = friendEmail;
        this.requestStatus = requestStatus;
        this.friendId = friendId;
        this.modifiedAt = LocalDateTime.now();
    }

    public void acceptFriendshipRequest() {
        friendshipStatus = FriendshipStatus.ACCEPTED;
    }
}


