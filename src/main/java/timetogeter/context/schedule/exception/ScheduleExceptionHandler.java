package timetogeter.context.schedule.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ScheduleExceptionHandler {
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle_ScheduleNotFoundException(ScheduleNotFoundException e) {
        log.error("ScheduleExceptionHandler.handle_ScheduleNotFoundException <{}> {}", e.getMessage(), e);
        return ErrorResponse.of(e.getStatus());
    }
}