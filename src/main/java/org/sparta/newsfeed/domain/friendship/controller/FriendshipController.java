package org.sparta.newsfeed.domain.friendship.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.config.JwtUtil;
import org.sparta.newsfeed.domain.friendship.dto.FriendshipRequestDto;
import org.sparta.newsfeed.domain.friendship.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

//        String loginUserEmail = jwtUtil.getEmailFromRequest(request);
        friendshipService.requestFriendship(requestDto, loginUserEmail);
        return ResponseEntity.ok("친구 요청 완료");
    }
}
