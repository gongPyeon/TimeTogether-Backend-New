package timetogeter.global.security.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class FilterExceptionHandler {
    @ExceptionHandler(AuthFailureException.class)
    public ResponseEntity<ErrorResponse> handle_DuplicateEmailException(AuthFailureException e) {
        log.error("DuplicateEmailExceptionHandler.handle_DuplicateEmailException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }
}
