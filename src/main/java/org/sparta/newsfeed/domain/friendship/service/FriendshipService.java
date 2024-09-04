package org.sparta.newsfeed.domain.friendship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.friendship.FriendshipRequestStatus;
import org.sparta.newsfeed.domain.friendship.FriendshipStatus;
import org.sparta.newsfeed.domain.friendship.dto.FriendshipRequestDto;
import org.sparta.newsfeed.domain.friendship.dto.FriendshipResponseDto;
import org.sparta.newsfeed.domain.friendship.entity.Friendship;
import org.sparta.newsfeed.domain.friendship.repository.FriendshipRepository;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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


        // 친구 요청 받은 유저
        User responseUser = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("검색한 이메일 계정은 존재하지 않습니다."));

        // 친구 요청 보낸 유저
        User requestUser = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 유저는 존재하지 않습니다."));


        // 자기 자신에게 친구 요청시
        if (requestUser.getId() == responseUser.getId()) {
            throw new IllegalArgumentException("자기 자신에게 친구 요청을 할 수 없습니다.");
        }
        // 받은 친구 요청
        Friendship receivedFriendship = Friendship.builder()
                .user(responseUser)
                .myEmail(requestDto.getEmail())
                .friendEmail(myEmail)
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
        responseUser.getFriendshipUserList().add(sendFriendship);
        requestUser.getFriendshipUserList().add(receivedFriendship);

        friendshipRepository.save(sendFriendship);
        friendshipRepository.save(receivedFriendship);



        log.info("{} 이메일 계정으로 {} 이메일 계정에 친구 요청을 완료하였습니다.", requestUser.getEmail(), responseUser.getEmail());
    }


    // 친구 요청 수락
    public void acceptFriendship(Long friendshipId) {

        // 친구 요청 수락할 요청
        Friendship acceptFriendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new NoSuchElementException("해당 친구 요청은 존재하지 않습니다."));

        // 이미 친구 관계인 경우 예외 처리
        if (acceptFriendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED) {
            throw new IllegalArgumentException("이미 친구 관계인 계정입니다.");
        }

        // 반대편 상대 요청을 찾기 위해 friendId 사용
        Friendship counterpartFriendship = friendshipRepository.findByUserIdAndFriendId(
                        acceptFriendship.getFriendId(), acceptFriendship.getUser().getId())
                .orElseThrow(() -> new NoSuchElementException("친구 요청 조회 실패"));

        // 양쪽의 상태를 ACCEPTED로 변경
        acceptFriendship.acceptFriendshipRequest();
        counterpartFriendship.acceptFriendshipRequest();

        log.info("{} 이메일 계정의 친구 요청을 수락하였습니다.", acceptFriendship.getUser().getEmail());
    }


    // 친구 요청 거절 및 친구 삭제
    public String rejectFriendship(Long friendshipId) {

        // 거절할 친구 요청, 혹은 삭제할 친구 관계 찾기
        Friendship rejectFriendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new NoSuchElementException("해당 친구 요청은 존재하지 않습니다."));

        // 반대편 상대의 친구 요청, 친구 관계 찾기
        Friendship counterpartFriendship = friendshipRepository.findByUserIdAndFriendId(
                        rejectFriendship.getFriendId(), rejectFriendship.getUser().getId())
                .orElseThrow(() -> new NoSuchElementException("친구 요청 조회 실패"));

        String res = rejectFriendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED ? "친구 삭제 완료" : "친구 요청 거절 완료";

        // 양쪽 친구 요청, 친구 관계 삭제
        friendshipRepository.delete(rejectFriendship);
        friendshipRepository.delete(counterpartFriendship);

        if (res.contains("삭제")) {
            log.info("{} 이메일 계정의 친구 요청을 삭제하였습니다.", rejectFriendship.getUser().getEmail());
        } else {
            log.info("{} 이메일 계정의 친구 요청을 거절하였습니다.", rejectFriendship.getUser().getEmail());
        }
        return res;

    }

    // 친구 리스트 조회
    public List<FriendshipResponseDto> getFriendsList(Long id, Pageable pageable) {
        List<Friendship> friendsList = friendshipRepository.findFriendsByUserId(id, pageable);
        return friendsList.stream()
                .map(FriendshipResponseDto::toFriendResponseDto)
                .toList();
    }

    // 미승인 친구 요청 리스트 조회
    public List<FriendshipResponseDto> getFriendRequestList(Long id, Pageable pageable) {
        List<Friendship> friendRequestList = friendshipRepository.findFriendRequestsByUserId(id, pageable);
        return friendRequestList.stream()
                .map(FriendshipResponseDto::toFriendResponseDto)
                .toList();
    }
}





