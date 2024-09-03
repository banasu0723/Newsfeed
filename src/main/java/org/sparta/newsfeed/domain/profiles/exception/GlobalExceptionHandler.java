package org.sparta.newsfeed.domain.profiles.exception;

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

    //CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        log.error(ex.getConsoleMessage());

        // MISSING_TOKEN 예외의 경우 401 Unauthorized 반환
        if (ex.getClientMessage().equals(ExceptionMessage.MISSING_TOKEN.getClientMessage())) {
            return new ResponseEntity<>(ex.getClientMessage(), HttpStatus.UNAUTHORIZED);
        }

        // 그 외의 CustomException 에 대해서는 400 Bad Request 반환
        return new ResponseEntity<>(ex.getClientMessage(), HttpStatus.BAD_REQUEST);
    }

    //MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>(); // 필드와 오류 메시지를 저장할 맵

        //필드 오류 순회하며 오류 정보 맵에 추가
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
            log.error(ExceptionMessage.VALIDATION_ERROR.getConsoleMessage(field, message)); // 콘솔에 유효성 검사 오류 메시지 로그
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 추가된 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        return new ResponseEntity<>("서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
