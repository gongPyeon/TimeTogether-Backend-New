package timetogeter.global.interceptor.response.error.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseCode implements StatusCode{
    OK(200, "요청에 성공했습니다.");

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    BaseCode(int code, String message) {
        this.code = code;
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }
}
