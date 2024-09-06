# 스프링N코드

---
![스크린샷 2024-09-03 오후 8 32 49](https://github.com/user-attachments/assets/ea9a9c96-e254-464f-ad64-d17a01825ec1)

# Newsfeed Web Page v1.0

---

## 배포 주소

[깃허브](https://github.com/banasu0723/Newsfeed)

---

## 💡 프로젝트 소개

본 프로젝트는 개인별 뉴스피드 조회를 바탕으로 한 SNS 입니다.<br>
회원 가입을 통해 사용자를 저장하고, 개인별 게시물을 등록합니다.<br>
개인은 타인과 친구 관계를 맺고 친구들의 게시물을 뉴스피드를 통해 조회할 수 있습니다.

# 🧐 팀구성원

| 이 름 | MBTI | 직책 |                  깃허브 주소                   |
|-----|:----:|:--:|:-----------------------------------------:|
| 이승언 | ENFP | 팀장 |   [이승언](https://github.com/banasu0723)    |
| 김도균 | INTP | 팀원 |     [김도균](https://github.com/gyun97)      |
| 김창민 | INFJ | 팀원 | [김창민](https://github.com/Rlackdals981010) |
| 나유화 | ENFJ | 팀원 |     [나유화](https://github.com/fargoe)      |

# 🚀STACK

Environment

![인텔리제이](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![깃이그노어](https://img.shields.io/badge/gitignore.io-204ECF?style=for-the-badge&logo=gitignore.io&logoColor=white)
![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

Development

![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![스프링](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![mysql](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)

Communication

![슬랙](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)

# 📊 화면 구성 (와이어프레임)

![와이어프레임](https://github.com/user-attachments/assets/bf7e5cc4-ffee-46bc-ba77-dff2c4d12190)

[와이어프레임 링크](https://www.figma.com/design/XRmxY5EYKGOYDjLjS4zHl0/Untitled?node-id=0-1&node-type=CANVAS&t=HnsX7ZEXYgMGJVDe-0)

# 📊 ERD

![image](https://github.com/user-attachments/assets/66931642-d6df-432a-bc4f-2de9027676b9)


# SQL(MySQL)

```sql
Table users {
  id bigint [primary key]
  email varchar(50) [not null, unique]
  password varchar(50) [not null]
  name varchar (20) [not null]
  image varchar (100)
  introduction varchar (255)
  activate boolean [not null]
  created_at timestamp  [not null]
  modified_at timestamp [not null]
}

Table posts {
  id bigint [primary key]
  user_id bigint  [not null]
  title varchar(100)  [not null]
  body text [note: 'Content of the post', not null] 
  created_at timestamp  [not null]
  modified_at timestamp [not null]
  like_count int [not null]

}

Table friendship {
  id bigint [primary key] 
  user_id bigint  [not null]
  friendship_status enum  [not null] //신청 수락 
  request_status enum [not null]
  friend_id bigint  [not null]
  created_at timestamp  [not null]
  modified_at timestamp [not null]
  my_email varchar(50) [not null]
  friend_email varchar(50) [not null]
}

Table likes {
  id bigint [primary key]
  post_id bigint [not null]
  comment_id bigint [not null]
  user_id bigint [not null]
}

Table comments {
  id bigint [primary key]
  post_id bigint [not null]
  user_id bigint [not null]
  body text [not null]
  created_at timestamp [not null]
  modified_at timestamp [not null]
  like_count int [not null]
}


Ref: posts.user_id > users.id // many-to-one

Ref: users.id < friendship.user_id

Ref: users.id < friendship.friend_id

Ref: comments.post_id > posts.id

Ref: comments.user_id > users.id

Ref: likes.comment_id > comments.id

Ref: likes.post_id > posts.id

Ref: likes.user_id > users.id



```

# 🏗️ 프로젝트 디렉토리 구조

```
├── README.md
├── build
│   ├── classes
│   │   └── java
│   │       └── main
│   │           └── org
│   │               └── sparta
│   │                   └── newsfeed
│   │                       ├── NewsfeedApplication.class
│   │                       ├── annotation
│   │                       │   └── Auth.class
│   │                       ├── config
│   │                       │   ├── AuthUserArgumentResolver.class
│   │                       │   ├── FilterConfig.class
│   │                       │   ├── JwtFilter.class
│   │                       │   ├── JwtUtil.class
│   │                       │   ├── PasswordEncoder.class
│   │                       │   └── WebConfig.class
│   │                       └── domain
│   │                           ├── auth
│   │                           │   ├── controller
│   │                           │   │   └── AuthController.class
│   │                           │   ├── dto
│   │                           │   │   ├── AuthUser.class
│   │                           │   │   ├── request
│   │                           │   │   │   ├── SignDeleteRequestDto.class
│   │                           │   │   │   ├── SigninRequestDto.class
│   │                           │   │   │   └── SignupRequestDto.class
│   │                           │   │   └── response
│   │                           │   │       ├── SigninResponseDto.class
│   │                           │   │       └── SignupResponseDto.class
│   │                           │   └── service
│   │                           │       └── AuthService.class
│   │                           ├── comments
│   │                           │   ├── controller
│   │                           │   │   └── CommentController.class
│   │                           │   ├── dto
│   │                           │   │   ├── CommentRequestDto.class
│   │                           │   │   └── CommentResponseDto.class
│   │                           │   ├── entity
│   │                           │   │   └── Comment.class
│   │                           │   ├── repository
│   │                           │   │   └── CommentRepository.class
│   │                           │   └── service
│   │                           │       └── CommentService.class
│   │                           ├── common
│   │                           │   ├── Timestamped.class
│   │                           │   └── exception
│   │                           │       ├── ApplicationException.class
│   │                           │       ├── ErrorCode.class
│   │                           │       ├── ErrorResponse.class
│   │                           │       └── GlobalExceptionHandler.class
│   │                           ├── friendship
│   │                           │   ├── FriendshipRequestStatus.class
│   │                           │   ├── FriendshipStatus.class
│   │                           │   ├── controller
│   │                           │   │   └── FriendshipController.class
│   │                           │   ├── dto
│   │                           │   │   ├── FriendshipRequestDto.class
│   │                           │   │   └── FriendshipResponseDto.class
│   │                           │   ├── entity
│   │                           │   │   ├── Friendship$FriendshipBuilder.class
│   │                           │   │   └── Friendship.class
│   │                           │   ├── repository
│   │                           │   │   └── FriendshipRepository.class
│   │                           │   └── service
│   │                           │       └── FriendshipService.class
│   │                           ├── likes
│   │                           │   ├── Like$LikeBuilder.class
│   │                           │   ├── Like.class
│   │                           │   ├── LikeController.class
│   │                           │   ├── LikeRepository.class
│   │                           │   └── LikeService.class
│   │                           ├── posts
│   │                           │   ├── controller
│   │                           │   │   └── PostController.class
│   │                           │   ├── dto
│   │                           │   │   ├── PostRequestDto.class
│   │                           │   │   ├── PostResponseDto$PostData.class
│   │                           │   │   └── PostResponseDto.class
│   │                           │   ├── entity
│   │                           │   │   ├── Post$PostBuilder.class
│   │                           │   │   └── Post.class
│   │                           │   ├── repository
│   │                           │   │   └── PostRepository.class
│   │                           │   └── service
│   │                           │       └── PostService.class
│   │                           ├── profiles
│   │                           │   ├── controller
│   │                           │   │   ├── ImageController.class
│   │                           │   │   └── ProfileController.class
│   │                           │   ├── dto
│   │                           │   │   ├── PasswordUpdateRequestDto.class
│   │                           │   │   ├── ProfileResponseDto.class
│   │                           │   │   └── ProfileUpdateRequestDto.class
│   │                           │   └── service
│   │                           │       └── ProfileService.class
│   │                           └── users
│   │                               ├── entity
│   │                               │   └── User.class
│   │                               └── repository
│   │                                   └── UserRepository.class
│   ├── generated
│   │   └── sources
│   │       ├── annotationProcessor
│   │       │   └── java
│   │       │       └── main
│   │       └── headers
│   │           └── java
│   │               └── main
│   ├── resources
│   │   └── main
│   │       ├── application-local.yaml
│   │       ├── application.yaml
│   │       └── static
│   │           └── images
│   │               └── 53d9f8fb-3aed-4c8b-85d2-57ba3fcf2541_image.png
│   └── tmp
│       └── compileJava
│           ├── compileTransaction
│           │   ├── backup-dir
│           │   └── stash-dir
│           │       
│           │       
│           │       
│           │      
│           │      
│           │      
│           │      
│           │      
│           │      
│           │      
│           └── previous-compilation-data.bin
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── sparta
    │   │           └── newsfeed
    │   │               ├── NewsfeedApplication.java
    │   │               ├── annotation
    │   │               │   └── Auth.java
    │   │               ├── config
    │   │               │   ├── AuthUserArgumentResolver.java
    │   │               │   ├── FilterConfig.java
    │   │               │   ├── JwtFilter.java
    │   │               │   ├── JwtUtil.java
    │   │               │   ├── PasswordEncoder.java
    │   │               │   └── WebConfig.java
    │   │               └── domain
    │   │                   ├── auth
    │   │                   │   ├── controller
    │   │                   │   │   └── AuthController.java
    │   │                   │   ├── dto
    │   │                   │   │   ├── AuthUser.java
    │   │                   │   │   ├── request
    │   │                   │   │   │   ├── SignDeleteRequestDto.java
    │   │                   │   │   │   ├── SigninRequestDto.java
    │   │                   │   │   │   └── SignupRequestDto.java
    │   │                   │   │   └── response
    │   │                   │   │       ├── SigninResponseDto.java
    │   │                   │   │       └── SignupResponseDto.java
    │   │                   │   └── service
    │   │                   │       └── AuthService.java
    │   │                   ├── comments
    │   │                   │   ├── controller
    │   │                   │   │   └── CommentController.java
    │   │                   │   ├── dto
    │   │                   │   │   ├── CommentRequestDto.java
    │   │                   │   │   └── CommentResponseDto.java
    │   │                   │   ├── entity
    │   │                   │   │   └── Comment.java
    │   │                   │   ├── repository
    │   │                   │   │   └── CommentRepository.java
    │   │                   │   └── service
    │   │                   │       └── CommentService.java
    │   │                   ├── common
    │   │                   │   ├── Timestamped.java
    │   │                   │   └── exception
    │   │                   │       ├── ApplicationException.java
    │   │                   │       ├── ErrorCode.java
    │   │                   │       ├── ErrorResponse.java
    │   │                   │       └── GlobalExceptionHandler.java
    │   │                   ├── friendship
    │   │                   │   ├── FriendshipRequestStatus.java
    │   │                   │   ├── FriendshipStatus.java
    │   │                   │   ├── controller
    │   │                   │   │   └── FriendshipController.java
    │   │                   │   ├── dto
    │   │                   │   │   ├── FriendshipRequestDto.java
    │   │                   │   │   └── FriendshipResponseDto.java
    │   │                   │   ├── entity
    │   │                   │   │   └── Friendship.java
    │   │                   │   ├── repository
    │   │                   │   │   └── FriendshipRepository.java
    │   │                   │   └── service
    │   │                   │       └── FriendshipService.java
    │   │                   ├── likes
    │   │                   │   ├── Like.java
    │   │                   │   ├── LikeController.java
    │   │                   │   ├── LikeRepository.java
    │   │                   │   └── LikeService.java
    │   │                   ├── posts
    │   │                   │   ├── controller
    │   │                   │   │   └── PostController.java
    │   │                   │   ├── dto
    │   │                   │   │   ├── PostRequestDto.java
    │   │                   │   │   └── PostResponseDto.java
    │   │                   │   ├── entity
    │   │                   │   │   └── Post.java
    │   │                   │   ├── repository
    │   │                   │   │   └── PostRepository.java
    │   │                   │   └── service
    │   │                   │       └── PostService.java
    │   │                   ├── profiles
    │   │                   │   ├── controller
    │   │                   │   │   ├── ImageController.java
    │   │                   │   │   └── ProfileController.java
    │   │                   │   ├── dto
    │   │                   │   │   ├── PasswordUpdateRequestDto.java
    │   │                   │   │   ├── ProfileResponseDto.java
    │   │                   │   │   └── ProfileUpdateRequestDto.java
    │   │                   │   └── service
    │   │                   │       └── ProfileService.java
    │   │                   └── users
    │   │                       ├── entity
    │   │                       │   └── User.java
    │   │                       └── repository
    │   │                           └── UserRepository.java
    │   └── resources
    │       ├── application-local.yaml
    │       ├── application.yaml
    │       └── static
    │           └── images
    │               └── 53d9f8fb-3aed-4c8b-85d2-57ba3fcf2541_image.png
    └── test
        └── java
            └── org
                └── sparta
                    └── newsfeed
                        └── NewsfeedApplicationTests.java

115 directories, 135 files
```

# API 명세

| 기능       | Method   | URL                                    | request | response      | 상태코드         |
|----------|----------|----------------------------------------|---------|---------------|--------------|
| 마이페이지 조회 | `GET`    | `/profiles/{id}`                       | Body    | 등록 정보         | `200 : 정상조회` |
| 프로필 수정   | `PUT`    | `/profiles/{id}`                       | Body    | 수정 정보         | `200 : 정상수정` |
| 비밀번호 변경  | `POST`   | `/profiles/{id}/change-pwd`            | Body    | 변경 메시지        | `200 : 정상수정` |
| 게시물 작성   | `POST`   | `/posts`                               | Body    | 등록 정보         | `201 : 정상작성` |
| 게시물 조회   | `GET`    | `/post/{id}`                           | Param   | 게시물 내용        | `200 : 정상조회` |
| 게시물 수정   | `PUT`    | `/posts/{id}`                          | Body    | 수정 정보         | `200 : 정상수정` |
| 게시물 삭제   | `DELETE` | `/posts/{id}`                          | Param   | 삭제 메시지        | `200 : 정상삭제` |
| 뉴스피드 조회  | `GET`    | `/posts`                               | Body    | 페이지네이션 된 뉴스피드 | `200 : 정상조회` |
| 회원 가입    | `POST`   | `/auth/signup`                         | Body    | 가입 메시지        | `201 : 정상가입` |
| 회원 탈퇴    | `POST`   | `/auth/signdelete`                     | Body    | 탈퇴 메시지        | `200 : 정상탈퇴` |
| 로그인      | `POST`   | `/auth/signin`                         | Body    | JWT           | `200 : 정상처리` |
| 친구 요청    | `POST`   | `/friendships`                         | Body    | 요청 메시지        | `200 : 정상요청` |
| 친구 수락    | `PATCH`  | `/friendships`                         | -       | 수락 메시지        | `200 : 정상요청` |
| 친구 삭제 및 거절    | `DELETE` | `/friendships/{friendshipsid}`         | -       | 거절(삭제) 메시지        | `200 : 정상삭제` |
| 친구 리스트   | `GET`    | `/friendships`                         | -       | 친구 리스트        | `200 : 정상조회` |
| 친구 요청 조회 |   `GET`   |`/freindships/request`                   | -       | -             | `200 : 정상조회` |
| 댓글 작성    | `POST`   | `/posts/{postId}/comments`             | Body    | 작성한 댓글 내용     | `200 : 정상생성` |
| 댓글 조회    | `GET`    | `/posts/{postId}/comments`             | -       | 댓글 내용         | `200 : 정상조회` |
| 댓글 수정    | `PUT`    | `/posts/{postId}/comments/{commentId}` | Body    | 수정한 댓글 내용     | `200 : 정상수정` |
| 댓글 삭제    | `DELETE` | `/posts/{postId}/comments/{commentId}` | -       | 삭제 메시지        | `200 : 정상삭제` |
| 뉴스피드 좋아요| `POST`    |`/likes/{postId}`                    |    -      | 좋아요 확인 및 취소 메시지   | `200 : 정상 반영` |
| 댓글 좋아요| `POST`    |`/likes/{postId}/{coomentId}`           |    -      | 좋아요 확인 및 취소 메시지   | `200 : 정상 반영` |

# ⚒️주요 기능

---
### **프로필 및 민감 정보 관리**

1. 프로필 조회
  > 프로필을 조회합니다.

2. 프로필 수정
  > 이름, 소개, 프로필 사진을 수정합니다. 
3. 비밀번호 수정
  >계정 비밀번호를 변경합니다.
---
### **게시물 관리**
1. 게시물 작성
  > 게시물 제목, 내용을 입력하여 게시물을 생성합니다.
2. 게시물 조회
  > 게시물 번호를 통해 게시물을 조회합니다.
3. 게시물 수정
  > 게시물의 제목, 내용을 수정합니다.
4. 게시물 삭제
  > 게시물을 삭제 합니다.
5. 뉴스피드 조회
  > 나와 친구관계인 유저의 게시물들을 생성일자 기준으로 내림차순 정렬합니다.

---
### **사용자 인증**
1. 회원 가입
  > 이메일 형식의 아이디와 비밀번호를 이용해 회원을 등록합니다.
2. 회원 탈퇴
  > 회원 계정의 상태를 activated = false 로 변환합니다.
3. 로그인
  > 이메일과 비밀번호가 일치시 JWT를 반환 받습니다.

~~4. 로그아웃~~(개발 취소)
  > ~~JWT의 만료 시점을 현 시점으로 변경합니다.~~

---
### **친구관계 관리**
1. 친구 요청
  > 상대방에게 친구 요청을 보냅니다.
2. 친구 수락
  > 친구 요청을 수락합니다.
3. 친구 삭제
  > 친구 관계를 종료합니다.
4. 친구 거절
  > 친구 요청을 거절합니다.
5. 친구 리스트
  > 친구 관계의 유저들을 조회합니다.
6. 친구 요청 조회
  > 친구 요청들을 조회합니다.

---
### **댓글 관련 기능**
1. 댓글 작성
  > 특정 게시물에 댓글을 작성합니다.
2. 댓글 조회
  > 특정 게시물에 저장된 댓글을 전부 조회합니다.
3. 댓글 수정
  > 댓글 작성자와 현 로그인 유저가 동일할 경우 해당 댓글을 수정합니다.
4. 댓글 삭제
  > 댓글 작성자와 현 로그인 유저가 동일할 경우 해당 댓글을 삭제합니다.


---
### **좋아요 관련 기능**
1. 뉴스피드 좋아요
  > 내 친구의 뉴스피드에 좋아요를 남기거나 취소할 수 있습니다.

2 . 댓글 좋아요
  > 본인이 작성한 댓글을 제외한 모든 댓글에 좋아요를 누르거나 취소할 수 있습니다.
