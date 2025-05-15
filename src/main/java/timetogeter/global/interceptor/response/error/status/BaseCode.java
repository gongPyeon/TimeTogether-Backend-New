package timetogeter.global.interceptor.response.error.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BaseCode implements StatusCode{
    OK(200, "요청에 성공했습니다.");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

}
