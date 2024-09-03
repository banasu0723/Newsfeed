package org.sparta.newsfeed.domain.newsfeed.service;

import org.sparta.newsfeed.domain.auth.dto.AuthUser;
import org.sparta.newsfeed.domain.posts.dto.response.PostResponseDto;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NewsfeedService {

//    private final UserRepository userRepository;
//
//    public NewsfeedService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public Page<PostResponseDto> getNewsfeed(int page, int size, AuthUser authUser) {
//        Pageable pageable = PageRequest.of(page-1,size);
//
//        User me = userRepository.findById(authUser.getId())
//                .orElseThrow(() -> new NullPointerException("잘못된 유저정보 입니다."));
//
//        //친구관계repository에서 친구 id 찾고
//
//        // 친구id의 post를 돌면서 제목과 이름만 넘겨보자
//
//
//    }
}
