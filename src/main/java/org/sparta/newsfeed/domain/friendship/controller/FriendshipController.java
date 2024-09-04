package org.sparta.newsfeed.domain.friendship.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.domain.friendship.dto.FriendshipRequestDto;
import org.sparta.newsfeed.domain.friendship.dto.FriendshipResponseDto;
import org.sparta.newsfeed.domain.friendship.service.FriendshipService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;


    // 친구 추가할 이메일 검색
    @GetMapping("/search")
    public ResponseEntity<String> findByEmail(@RequestBody FriendshipRequestDto requestDto) {
        String findEmail = friendshipService.findByEmail(requestDto.getEmail());
        log.info("검색한 이메일 계정 {}은 존재하는 계정입니다.", findEmail);
        return ResponseEntity.ok(findEmail);
    }

    // 찾은 이메일 계정에 친구 요청
    @PostMapping
    public ResponseEntity<String> requestFriendship(
            @RequestBody @Valid FriendshipRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("이메일이 입력되지 않았거나 잘못된 이메일 형식입니다.");
        }

        String loginUserEmail = request.getAttribute("email").toString();

        friendshipService.requestFriendship(requestDto, loginUserEmail);
        return ResponseEntity.ok("친구 요청 완료");
    }

    // 친구 요청 수락
    @PutMapping("/{friendshipId}")
    public ResponseEntity<String> acceptFriendship(@PathVariable("friendshipId") Long friendshipId) {

        friendshipService.acceptFriendship(friendshipId);
        return ResponseEntity.ok("친구 요청 수락 완료");
    }

    // 친구 요청 거절 및 친구 삭제
    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<String> rejectFriendship(@PathVariable Long friendshipId) {

        String res = friendshipService.rejectFriendship(friendshipId);
        return ResponseEntity.ok(res);
    }

    // 친구 리스트 조회
    @GetMapping
    public ResponseEntity<List<FriendshipResponseDto>> getFriendsList(HttpServletRequest httpRequest, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        List<FriendshipResponseDto> res = friendshipService.getFriendsList(userId, pageable);
        return ResponseEntity.ok(res);
    }

    // 친구 요청 리스트 조회
    @GetMapping("/request")
    public ResponseEntity<List<FriendshipResponseDto>> getFriendRequestList(HttpServletRequest httpRequest, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        List<FriendshipResponseDto> res = friendshipService.getFriendRequestList(userId, pageable);
        return ResponseEntity.ok(res);
    }



}
