package timetogeter.global.interceptor.response.error;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.interceptor.response.error.status.StatusCode;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    //로그인 예외 처리
    /*
    //예시
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
     */


    //그룹 예외 처리


    //===================================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        StatusCode errorCode = BaseErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        StatusCode errorCode = BaseErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<?> handleExceptionInternal(StatusCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(StatusCode errorCode) {
        return ErrorResponse.builder()
                .name(errorCode.name())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    private ResponseEntity<?> handleExceptionInternal(StatusCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(StatusCode errorCode, String message) {
        return ErrorResponse.builder()
                .name(errorCode.name())
                .code(errorCode.getCode())
                .message(message)
                .build();
    }
}
