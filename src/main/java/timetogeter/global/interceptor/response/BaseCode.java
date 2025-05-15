package timetogeter.global.interceptor.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import timetogeter.global.interceptor.response.StatusCode;

@Getter
public enum BaseCode implements StatusCode {
    OK("요청에 성공했습니다."),
    SUCCESS_SIGN_UP("회원가입에 성공했어요"),
    SUCCESS_ID("사용 가능한 아이디에요");

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    BaseCode(String message) {
        this.code = 200;
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }
}
