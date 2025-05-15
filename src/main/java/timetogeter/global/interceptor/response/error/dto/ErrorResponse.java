package timetogeter.global.interceptor.response.error.dto;

import lombok.*;
import org.springframework.http.ResponseEntity;
import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.StatusCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder // 이거의 필요성이 있나?
public class ErrorResponse {
    private final int code;
    private final String message;

    public static ResponseEntity<ErrorResponse> of(StatusCode code) {
        ErrorResponse res = new ErrorResponse(code.getCode(), code.getMessage());
        return new ResponseEntity<>(res, code.getHttpStatus());
    }

    public static ResponseEntity<ErrorResponse> of(CustomException exception) {
        ErrorResponse res = new ErrorResponse(exception.getStatus().getCode(), exception.getMessage());

        return new ResponseEntity<>(res, exception.getStatus().getHttpStatus());
    }
}
