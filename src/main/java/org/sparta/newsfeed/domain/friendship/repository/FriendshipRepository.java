package org.sparta.newsfeed.domain.friendship.repository;

import org.sparta.newsfeed.domain.friendship.entity.Friendship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {


    // 친구 ID와 유저 ID로 친구 관계 확인 메서드
    Optional<Friendship> findByUserIdAndFriendId(Long friendId, Long id);

    // 친구 요청 (아직 요청 수락되지 않은) 리스트 반환 쿼리 메서드
    @Query("SELECT f FROM Friendship f WHERE f.user.id = :userId AND f.friendshipStatus = 'PENDING'")
    List<Friendship> findFriendRequestsByUserId(Long userId, Pageable pageable);

    // 친구 (이미 요청 수락된) 리스트 반환 쿼리 메서드
    @Query("SELECT f FROM Friendship f WHERE f.user.id = :userId AND f.friendshipStatus = 'ACCEPTED'")
    List<Friendship> findFriendsByUserId(Long userId, Pageable pageable);

    // 특정 사용자와 친구인 사용자들의 ID를 모두 가져오는 쿼리 메서드
    @Query("SELECT f.friendId FROM Friendship f WHERE f.user.id = :userId AND f.friendshipStatus = 'ACCEPTED'")
    List<Long> findAllFriendIdsByUserId(Long userId);
}
