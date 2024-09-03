package org.sparta.newsfeed.domain.friendship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.friendship.FriendshipRequestStatus;
import org.sparta.newsfeed.domain.friendship.FriendshipStatus;
import org.sparta.newsfeed.domain.friendship.dto.FriendshipRequestDto;
import org.sparta.newsfeed.domain.friendship.repository.FriendshipRepository;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.sparta.newsfeed.domain.friendship.entity.Friendship;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;


    // 이메일 검색
    public String findByEmail(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("검색한 이메일 계정은 존재하지 않습니다."));

        return user.getEmail();

    }

    // 친구 요청
    public void requestFriendship(FriendshipRequestDto requestDto, String myEmail) {

        // 친구 요청 보낸 유저
        User requestUser = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 유저는 존재하지 않습니다."));

        // 친구 요청 받은 유저
        User responseUser = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("검색한 이메일 계정은 존재하지 않습니다."));

        // 자기 자신에게 친구 요청시
        if (requestUser.getId() == responseUser.getId()) {
            throw new IllegalArgumentException("자기 자신에게 친구 요청을 할 수 없습니다.");
        }

        // 받은 친구 요청
        Friendship receivedFriendship = Friendship.builder()
                .user(responseUser)
                .myEmail(responseUser.getEmail())
                .friendEmail(requestUser.getEmail())
                .friendId(requestUser.getId())
                .friendshipStatus(FriendshipStatus.PENDING)
                .requestStatus(FriendshipRequestStatus.RECEIVED) // 요청을 받았음

                .build();

        // 보낸 친구 요청
        Friendship sendFriendship = Friendship.builder()
                .user(requestUser)
                .myEmail(requestUser.getEmail())
                .friendEmail(responseUser.getEmail())
                .friendId(responseUser.getId())
                .friendshipStatus(FriendshipStatus.PENDING)
                .requestStatus(FriendshipRequestStatus.SENT) // 요청을 보냈음
                .build();

        //각각의 유저에 친구 요청 저장
        requestUser.getFriendshipUserList().add(sendFriendship);
        responseUser.getFriendshipUserList().add(receivedFriendship);

        friendshipRepository.save(sendFriendship);
        friendshipRepository.save(receivedFriendship);


        log.info("{} 이메일 계정으로 {} 이메일 계정에 친구 요청을 완료하였습니다.", requestUser.getEmail(), responseUser.getEmail());
    }

    // 친구 요청 수락
    public void acceptFriendship(Long friendshipId) {


        // 친구 요청 수락할 요청
        Friendship acceptFriendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new NoSuchElementException("해당 친구 요청은 존재하지 않습니다."));

        // 반대편 상대 요청
        Friendship counterpartFriendship = friendshipRepository.findById(acceptFriendship.getFriendId())
                .orElseThrow(() -> new NoSuchElementException("친구 요청 조회 실패"));

        if (acceptFriendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED) {
            throw new IllegalArgumentException("이미 친구 관계인 계정입니다.");
        }

        acceptFriendship.acceptFriendshipRequest();
        counterpartFriendship.acceptFriendshipRequest();

    }

    // 친구 요청 거절
    public void rejectFriendship(Long friendshipId) {

        Friendship rejectFriendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new NoSuchElementException("해당 친구 요청은 존재하지 않습니다."));

        Friendship counterpartFriendship = friendshipRepository.findById(rejectFriendship.getFriendId())
                .orElseThrow(() -> new NoSuchElementException("친구 요청 조회 실패"));

        User rejectingUser = userRepository.findById(rejectFriendship.getUser().getId()).get();
        User rejectedUser = userRepository.findById(counterpartFriendship.getUser().getId()).get();

        rejectingUser.getFriendshipUserList().remove(rejectFriendship);
        rejectedUser.getFriendshipUserList().remove(counterpartFriendship);

        friendshipRepository.delete(rejectFriendship);
        friendshipRepository.delete(counterpartFriendship);


    }
}
