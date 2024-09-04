package org.sparta.newsfeed.domain.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        // 클라이언트에 보내는 오류 메시지와 상태 코드
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("message", ex.getClientMessage());  // 오류 메시지
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());  // 상태 코드

        // 로그 메시지의 형식을 통일화
        log.error("Error: {}", ex.getConsoleMessage());

        // MISSING_TOKEN 예외의 경우 401 Unauthorized 반환
        if (ex.getClientMessage().equals(ExceptionMessage.MISSING_TOKEN.getClientMessage())) {
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);

            // 로그 메시지의 형식을 통일화
            log.error("Validation Error: Field = {}, Error = {}", field, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 그 외의 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        log.error("Unexpected Error: ", ex);

        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("message", "서버에 오류가 발생했습니다.");
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
