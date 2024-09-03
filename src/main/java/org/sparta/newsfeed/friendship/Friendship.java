package org.sparta.newsfeed.friendship;

import jakarta.persistence.*;
import lombok.*;
import org.sparta.newsfeed.timestamp.TimeStamped;
import org.sparta.newsfeed.users.User;


/**
 * user: 본인 , friend : 요청 받은 상대방 또는 요청한 상대방
 */
@Entity
@Table(name = "friendship")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship extends TimeStamped {

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

    private String userEmail;
    private String friendEmail;
    private boolean isFrom;

    @Column(name = "friend_id", nullable = false)
    private Long friendId;


    @Builder
    public Friendship(Long id, User user, User friend, FriendshipStatus status, String userEmail, String friendEmail, boolean isFrom, Long friendId) {
        this.id = id;
        this.user = user;
//        this.friend = friend;
        this.status = status;
        this.userEmail = userEmail;
        this.friendEmail = friendEmail;
        this.isFrom = isFrom;
        this.friendId = friendId;
    }

    public void acceptFriendshipRequest() {
        status = FriendshipStatus.ACCEPTED;
    }




}


