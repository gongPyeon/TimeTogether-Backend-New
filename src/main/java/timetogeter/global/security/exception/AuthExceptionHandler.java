package timetogeter.global.security.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogeter.context.auth.application.exception.RedisException;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AuthExceptionHandler {
    @ExceptionHandler(AuthFailureException.class)
    public ResponseEntity<ErrorResponse> handle_AuthFailureException(AuthFailureException e) {
        log.error("AuthExceptionHandler.handle_AuthFailureException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ErrorResponse> handle_InvalidJwtException(InvalidJwtException e) {
        log.error("AuthExceptionHandler.handle_InvalidJwtException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_UserNotFoundException(UserNotFoundException e) {
        log.error("AuthExceptionHandler.handle_UserNotFoundException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<ErrorResponse> handle_RedisException(RedisException e) {
        log.error("AuthExceptionHandler.handle_RedisException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }
}
