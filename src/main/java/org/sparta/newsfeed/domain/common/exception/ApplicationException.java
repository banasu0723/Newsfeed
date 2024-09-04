package org.sparta.newsfeed.domain.common.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final HttpStatus status;
    private final String clientMessage;

    // 기존 생성자
    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.clientMessage = errorCode.getMessage();
    }

    // 새로운 생성자: 추가 메시지 지원
    public ApplicationException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.status = errorCode.getStatus();
        this.clientMessage = customMessage; // 사용자 정의 메시지를 클라이언트로 반환
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
