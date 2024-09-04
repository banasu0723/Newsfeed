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
    FORBIDDEN_PROFILE_ACCESS("접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;  // 포스트맨에 반환할 메시지
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    // 메시지를 포맷팅할 수 있도록 도와주는 메서드 (필요에 따라 사용)
    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}
