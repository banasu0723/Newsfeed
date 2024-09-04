package org.sparta.newsfeed.domain.common.exception;

public enum ExceptionMessage {

    USER_NOT_FOUND("사용자 조회 실패 - 해당 ID(%s)의 사용자를 찾을 수 없습니다.", "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD("비밀번호 확인 실패 - 입력된 현재 비밀번호가 ID(%s)의 저장된 비밀번호와 일치하지 않습니다.", "현재 비밀번호가 올바르지 않습니다."),
    SAME_PASSWORD("비밀번호 변경 실패 - 새 비밀번호가 현재 비밀번호와 동일합니다. ID(%s)", "새 비밀번호는 현재 비밀번호와 동일할 수 없습니다."),
    INVALID_TOKEN("토큰 검증 실패 - 유효하지 않은 토큰(%s)이 제공되었습니다.", "유효하지 않은 토큰입니다. 요청이 거부되었습니다."),
    MISSING_TOKEN("토큰이 제공되지 않았습니다.", "토큰이 필요합니다."),
    VALIDATION_ERROR("입력 값 검증 실패 - 필드명(%s), 문제(%s)", "잘못된 요청입니다."),
    GENERAL_EXCEPTION("서버 오류 발생 : %s", "서버 오류가 발생했습니다."),
    FORBIDDEN_PROFILE_ACCESS("프로필 접근 실패 - 접근 권한이 없습니다.", "접근 권한이 없습니다.");

    private final String consoleMessage;
    private final String clientMessage;

    ExceptionMessage(String consoleMessage, String clientMessage) {
        this.consoleMessage = consoleMessage;
        this.clientMessage = clientMessage;
    }

    public String getConsoleMessage(Object... args) {
        return String.format(consoleMessage, args);
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
