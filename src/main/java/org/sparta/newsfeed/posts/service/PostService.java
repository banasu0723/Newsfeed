package org.sparta.newsfeed.posts.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.posts.dto.PostRequestDto;
import org.sparta.newsfeed.posts.dto.PostResponseDto;
import org.sparta.newsfeed.posts.entity.Post;
import org.sparta.newsfeed.posts.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private PostRepository postRepository;

    //게시물 작성
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        //User user = getAuthenticatedUser();  // 현재 인증된 사용자 정보 가져오기
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());  // 현재 타임스탬프 가져오기
        Post post = Post.builder()
                //.user(user)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .createdAt(currentTimestamp)
                .updatedAt(currentTimestamp)
                .build();  // 새로운 게시물 객체 생성
        postRepository.save(post);  // 게시물 저장
        PostResponseDto.PostData postData = new PostResponseDto.PostData(post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt());  // 응답 데이터 설정
        return new PostResponseDto(200, "게시물 작성 완료.", postData);  // 응답 DTO 반환
    }
}
