package org.sparta.newsfeed.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sparta.newsfeed.users.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;


    public String findByEmail(String userEmail) {
        User searchUser = friendshipRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("검색한 이메일 계정은 존재하지 않습니다."));
        return searchUser.getEmail();

    }

    public void requestFriendship(@RequestBody FriendshipRequestDto requestDto, @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 유저는 존재하지 않습니다."));

        User friend = friendshipRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("검색한 이메일 계정은 존재하지 않습니다."));

        Friendship friendship = Friendship.builder()
                .user(user)
                .friend(friend)
                .status()
                .build();



    }

}
