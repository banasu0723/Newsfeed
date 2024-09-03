package org.sparta.newsfeed.friendship;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/friendships")
public class FriendshipController {


    private final FriendshipService friendshipService;

    @GetMapping("/search")
    public ResponseEntity<String> findByEmail(@RequestBody String userEmail) {
        String findEmail = friendshipService.findByEmail(userEmail);
        return ResponseEntity.ok(findEmail);
    }

    @PostMapping
    public ResponseEntity<String> requestFriendship(
            @RequestBody @Valid FriendshipRequestDto requestDto,
            BindingResult bindingResult,
            @PathVariable Long id) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("이메일이 입력되지 않거나 잘못된 이메일 형식입니다.");
        }
        friendshipService.requestFriendship(requestDto, id);
        return ResponseEntity.ok("친구 요청 완료");
    }
}
