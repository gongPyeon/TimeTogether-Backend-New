package timetogeter.context.group.application.exception;

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
public class GroupExceptionHandler {
    @ExceptionHandler(GroupIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_GroupIdNotFoundException(GroupIdNotFoundException e) {
        log.error("GroupExceptionHandler.handle_GroupIdNotFoundException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(GroupShareKeyException.class)
    public ResponseEntity<ErrorResponse> handle_GroupShareKeyException(GroupShareKeyException e) {
        log.error("GroupExceptionHandler.handle_GroupShareKeyException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(GroupIdDecryptException.class)
    public ResponseEntity<ErrorResponse> handle_GroupIdDecryptException(GroupIdDecryptException e) {
        log.error("GroupExceptionHandler.handle_GroupIdDecryptException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }
}
