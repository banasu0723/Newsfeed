package org.sparta.newsfeed.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    SAME_PASSWORD("새 비밀번호는 현재 비밀번호와 동일할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    MISSING_TOKEN("토큰이 필요합니다.", HttpStatus.UNAUTHORIZED),
    VALIDATION_ERROR("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    GENERAL_EXCEPTION("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN_PROFILE_ACCESS("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_COMMENT_ACCESS("댓글에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SAME_USER("자기 자신에게 친구 신청을 할 수 없습니다", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_FOUND("해당 요청을 찾을 수 없습니다", HttpStatus.BAD_REQUEST),
    ALREADY_ACCEPTED("이미 친구 관계인 유저와는 친구 요청 및 수락을 할 수 없습니다", HttpStatus.BAD_REQUEST),
    ALREADY_REQUESTED("이미 친구 요청한 유저에게는 친구 요청을 할 수 없습니다", HttpStatus.BAD_REQUEST),
    RECEIVED_ONLY("친구 요청은 받은 본인만 수락할 수 있습니다", HttpStatus.BAD_REQUEST),
    FORBIDDEN_FRIENDSHIP_ACCESS("친구 요청 취소 혹은 친구 삭제는 당사자간에만 가능합니다.", HttpStatus.BAD_REQUEST),
    NOT_MY_POST("본인의 게시글에는 '좋아요'를 남길 수 없습니다", HttpStatus.BAD_REQUEST),
    NOT_MY_COMMENT("본인의 댓글에는 '좋아요'를 남길 수 없습니다", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    // 메시지를 포맷팅할 수 있도록 도와주는 메서드
    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}
