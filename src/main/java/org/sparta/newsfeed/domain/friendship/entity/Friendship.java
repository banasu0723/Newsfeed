package org.sparta.newsfeed.domain.friendship.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sparta.newsfeed.domain.friendship.FriendshipStatus;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.common.Timestamped;

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


//    @Setter
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "friend_id", nullable = false)
//    private User friend;

    @Enumerated
    @Column(name = "status", nullable = false)
    private FriendshipStatus status;

    private String myEmail;
    private String friendEmail;
    private boolean isReceived; // 요청을 받았는지 보냈는지 구분

    @Column(name = "friend_id", nullable = false)
    private Long friendId;


    @Builder
    public Friendship(Long id, User user, User friend, FriendshipStatus status, String myEmail, String friendEmail, boolean isReceived, Long friendId) {
        this.id = id;
        this.user = user;
//        this.friend = friend;
        this.status = status;
        this.myEmail = myEmail;
        this.friendEmail = friendEmail;
        this.isReceived = isReceived;
        this.friendId = friendId;
    }

    public void acceptFriendshipRequest() {
        status = FriendshipStatus.ACCEPTED;
    }




}


