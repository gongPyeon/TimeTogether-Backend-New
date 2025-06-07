package timetogeter.context.place.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogeter.context.auth.exception.AuthFailureException;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class PlaceExceptionHandler {
    @ExceptionHandler(InvalidPlaceNumException.class)
    public ResponseEntity<ErrorResponse> handle_InvalidPlaceNumException(InvalidPlaceNumException e) {
        log.error("PlaceExceptionHandler.handle_InvalidPlaceNumException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_PlaceNotFoundException(PlaceNotFoundException e) {
        log.error("PlaceExceptionHandler.handle_PlaceNotFoundException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(PlaceUserIdNotSame.class)
    public ResponseEntity<ErrorResponse> handle_PlaceUserIdNotSame(PlaceUserIdNotSame e) {
        log.error("PlaceExceptionHandler.handle_PlaceUserIdNotSame <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(VoteFailException.class)
    public ResponseEntity<ErrorResponse> handle_VoteFailException(VoteFailException e) {
        log.error("PlaceExceptionHandler.handle_VoteFailException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(VoteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_VoteNotFoundException(VoteNotFoundException e) {
        log.error("PlaceExceptionHandler.handle_VoteNotFoundException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }
}