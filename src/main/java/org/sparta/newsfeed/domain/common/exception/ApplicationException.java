package org.sparta.newsfeed.domain.common.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final HttpStatus status;
    private final String clientMessage;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.clientMessage = errorCode.getMessage();
    }

    // 메시지 포맷팅을 위한 추가 생성자
    public ApplicationException(ErrorCode errorCode, Object... args) {
        super(errorCode.getFormattedMessage(args));
        this.status = errorCode.getStatus();
        this.clientMessage = errorCode.getFormattedMessage(args);  // 클라이언트에게 포맷팅된 메시지 제공
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
