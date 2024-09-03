package org.sparta.newsfeed.domain.newsfeed.controller;

import org.sparta.newsfeed.annotation.Auth;
import org.sparta.newsfeed.domain.auth.dto.AuthUser;
import org.sparta.newsfeed.domain.newsfeed.service.NewsfeedService;
import org.sparta.newsfeed.domain.posts.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsfeedController {
    private final NewsfeedService newsfeedService;

    public NewsfeedController(NewsfeedService newsfeedService) {
        this.newsfeedService = newsfeedService;
    }

//    @GetMapping("/posts")
//    public ResponseEntity<Page<PostResponseDto>> getNewsfeed(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @Auth AuthUser authUser
//    ){
//        return ResponseEntity.ok(newsfeedService.getNewsfeed(page, size,authUser));
//    }

}
