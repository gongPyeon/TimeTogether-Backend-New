package timetogeter.context.auth.application.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;
import timetogeter.global.security.exception.AuthFailureException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AuthExceptionHandler {
    @ExceptionHandler(AuthFailureException.class)
    public ResponseEntity<ErrorResponse> handle_AuthFailureException(AuthFailureException e) {
        log.error("AuthExceptionHandler.handle_AuthFailureException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }
}
