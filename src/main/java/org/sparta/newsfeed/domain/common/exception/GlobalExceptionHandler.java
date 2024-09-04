package org.sparta.newsfeed.domain.common.exception;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // ApplicationException 처리
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        log.error("애플리케이션 예외 발생: {}", ex.getMessage());  // 로그 기록
        return new ResponseEntity<>(new ErrorResponse(ex.getClientMessage(), ex.getStatus().value()), ex.getStatus());
    }

    // 만료된 JWT 토큰 예외 처리
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("만료된 JWT 예외 발생: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    // JWT 관련 일반 예외 처리
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        log.error("JWT 예외 발생: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    // 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = "잘못된 요청입니다.";

        if (fieldError != null) {
            errorMessage = fieldError.getDefaultMessage();  // DTO에서 설정한 메시지를 사용
        }

        log.error("유효성 검사 오류 발생: {}", errorMessage);
        return new ResponseEntity<>(new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    // 필수 파라미터 누락 예외 처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("필수 파라미터 누락: {}", ex.getParameterName());
        String errorMessage = String.format("필수 요청 파라미터 '%s'가 누락되었습니다.", ex.getParameterName());
        return new ResponseEntity<>(new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    // 기타 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("서버 오류 발생: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.GENERAL_EXCEPTION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
