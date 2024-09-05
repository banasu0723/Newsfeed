package org.sparta.newsfeed.domain.friendship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.common.exception.ApplicationException;
import org.sparta.newsfeed.domain.common.exception.ErrorCode;
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
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        return user.getEmail();
    }

    // 친구 요청
    public void requestFriendship(FriendshipRequestDto requestDto, String myEmail) {


        // 친구 요청 받은 유저
        User responseUser = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 친구 요청 보낸 유저
        User requestUser = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        Optional<Friendship> checkFriendship = friendshipRepository.findByUserIdAndFriendId(responseUser.getId(), requestUser.getId());

        // 친구 관계 혹은 요청 존재하는지 확인
        if (checkFriendship.isPresent()) {
            if (checkFriendship.get().getFriendshipStatus() == FriendshipStatus.PENDING) { // 친구 요청만 존재하는 경우
                throw new ApplicationException((ErrorCode.ALREADY_REQUESTED));
            } else { // 이미 친구 관계인 경우
                throw new ApplicationException(ErrorCode.ALREADY_ACCEPTED);
            }

        }
        // 자기 자신에게 친구 요청시
        if (requestUser.getId().equals(responseUser.getId())) {
            throw new ApplicationException(ErrorCode.SAME_USER);
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
    public void acceptFriendship(Long friendshipId, String email) {

        // 친구 요청 수락할 요청
        Friendship acceptFriendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.REQUEST_NOT_FOUND));

        // 받은(Received) 친구 요청만 수락 가능 -> 보내진(Sent) 친구 요청은 수락 불가
        if (acceptFriendship.getRequestStatus() == FriendshipRequestStatus.SENT) {
            throw new ApplicationException(ErrorCode.RECEIVED_ONLY);
        }

        log.info("User ID: {}, Friend ID: {}", email, acceptFriendship.getMyEmail());
        // 친구 요청 받은 본인만 로그인하여 해당 요청 수락 가능
        if (!email.equals(acceptFriendship.getMyEmail())) {
            throw new ApplicationException(ErrorCode.RECEIVED_ONLY);
        }

        // 이미 친구 관계인 경우 예외 처리
        if (acceptFriendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED) {
            throw new ApplicationException(ErrorCode.ALREADY_ACCEPTED);
        }

        // 반대편 상대 요청을 찾기 위해 friendId 사용
        Friendship counterpartFriendship = friendshipRepository.findByUserIdAndFriendId(
                        acceptFriendship.getFriendId(), acceptFriendship.getUser().getId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.REQUEST_NOT_FOUND));

        // 양쪽의 상태를 ACCEPTED(친구 수락)로 변경
        acceptFriendship.acceptFriendshipRequest();
        counterpartFriendship.acceptFriendshipRequest();

        log.info("{} 이메일 계정의 친구 요청을 수락하였습니다.", acceptFriendship.getUser().getEmail());
    }


    // 친구 요청 거절 및 친구 삭제
    public String rejectFriendship(Long friendshipId, String email) {

        // 거절할 친구 요청, 혹은 삭제할 친구 관계 찾기
        Friendship rejectFriendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.REQUEST_NOT_FOUND));

        // 반대편 상대의 친구 요청, 친구 관계 찾기
        Friendship counterpartFriendship = friendshipRepository.findByUserIdAndFriendId(
                        rejectFriendship.getFriendId(), rejectFriendship.getUser().getId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.REQUEST_NOT_FOUND));

        // 현재 로그인한 사용자만 본인과 관련한 친구 요청 취소, 거절 또는 친구 삭제 가능
        if (!email.equals(rejectFriendship.getMyEmail())) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_FRIENDSHIP_ACCESS);
        }

        String res = rejectFriendship.getFriendshipStatus() == FriendshipStatus.ACCEPTED ? "친구 삭제 완료" : "친구 요청 거절(취소) 완료";

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





