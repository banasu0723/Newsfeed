package org.sparta.newsfeed.domain.posts.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.annotation.Auth;
import org.sparta.newsfeed.domain.auth.dto.AuthUser;
import org.sparta.newsfeed.domain.auth.service.AuthService;
import org.sparta.newsfeed.domain.common.exception.ApplicationException;
import org.sparta.newsfeed.domain.common.exception.ErrorCode;
import org.sparta.newsfeed.domain.friendship.repository.FriendshipRepository;
import org.sparta.newsfeed.domain.posts.dto.PostRequestDto;
import org.sparta.newsfeed.domain.posts.dto.PostResponseDto;
import org.sparta.newsfeed.domain.posts.entity.Post;
import org.sparta.newsfeed.domain.posts.repository.PostRepository;
import org.sparta.newsfeed.domain.users.entity.User;
import org.sparta.newsfeed.domain.users.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final FriendshipRepository friendshipRepository;


    //게시물 작성
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long userId) {
        //User user = getAuthenticatedUser();  // 현재 인증된 사용자 정보 가져오기
        User user = userRepository.findById(userId).get();

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());  // 현재 타임스탬프 가져오기
        Post post = Post.builder()
                .user(user)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .createdAt(currentTimestamp)
                .updatedAt(currentTimestamp)
                .build();  // 새로운 게시물 객체 생성
        postRepository.save(post);  // 게시물 저장
        PostResponseDto.PostData postData = new PostResponseDto.PostData(post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt());  // 응답 데이터 설정
        return new PostResponseDto(200, "게시물 작성 완료.", postData);  // 응답 DTO 반환
    }

    // 친구의 뉴스피드만 가져오기
    public List<PostResponseDto> getFriendNewsfeeds(Long userId, Pageable pageable) {

        // 사용자의 친구 목록에서 친구들의 ID를 가져옴
        List<Long> friendIds = friendshipRepository.findAllFriendIdsByUserId(userId);

        // 친구들의 ID에 해당하는 게시물만 필터링하여 반환(SELECT * FROM Post WHERE user_id IN (?, ?, ?, ...) LIMIT 10 OFFSET 0;)
        List<Post> friendPosts = postRepository.findByUserIdIn(friendIds, pageable);

        // 가져온 친구 게시물들 Dto로 변환하면 반환
        return friendPosts.stream()
                .map(post -> {
                    PostResponseDto.PostData postData = new PostResponseDto.PostData(
                            post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt()
                    );
                    return new PostResponseDto(200, "친구의 뉴스피드 조회 성공", postData);
                })
                .collect(Collectors.toList());
    }

    //게시물 수정
    public PostResponseDto updatePost(@Auth AuthUser authUser, Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시물이 없습니다."));  // 게시물 조회

        if (!post.getUser().getId().equals(authUser.getId())) { // post의 제작자가, 현재 로그인한 사람이 아니면
            throw new ApplicationException(ErrorCode.FORBIDDEN_COMMENT_ACCESS); // 예외 처리
        }

        post.update(postRequestDto.getTitle(), postRequestDto.getContent());  // 게시물 수정
        postRepository.save(post);  // 수정된 게시물 저장
        PostResponseDto.PostData postData = new PostResponseDto.PostData(post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt());  // 응답 데이터 설정
        return new PostResponseDto(200, "게시물 수정 완료.", postData);  // 응답 DTO 반환
    }

    // 내 게시글 조회
    public List<PostResponseDto> getMyPosts(Long userId, Pageable pageable) {
        List<Post> myPost = postRepository.findByUserId(userId, pageable);

        return myPost.stream()
                .map(post -> {
                    PostResponseDto.PostData postData = new PostResponseDto.PostData(
                            post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt()
                    );
                    return new PostResponseDto(200, "내 게시물 조회 성공", postData);
                })
                .collect(Collectors.toList());
    }

    // 게시물 삭제
    public void deletePost(AuthUser authUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시물이 없습니다"));  // 게시물 조회

        if (!post.getUser().getId().equals(authUser.getId())) { // post의 제작자가, 현재 로그인한 사람이 아니면
            throw new ApplicationException(ErrorCode.FORBIDDEN_COMMENT_ACCESS); // 예외 처리
        }

        postRepository.delete(post);  // 게시물 삭제
    }

    // 게시물 작성자인지 확인
    public boolean checkPostOwnership(@Auth AuthUser authUser, Long postId, Long userId) {
        authUser.getEmail();
        return postRepository.findById(postId)
                .map(post -> post.getUser().getId().equals(userId))
                .orElse(false);  // 게시물이 없으면 false 반환
    }
}
