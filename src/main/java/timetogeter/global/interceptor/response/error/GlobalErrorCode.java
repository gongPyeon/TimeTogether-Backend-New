package timetogeter.global.interceptor.response.error;

import lombok.*;
import org.springframework.http.HttpStatus;
import timetogeter.global.interceptor.response.error.errorbase.ErrorCode;

//필요한 에러를 //[에러이름] 에러 란에 만들기
@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    //Success
    OK(0000, HttpStatus.OK, "OK"),

    //공통 에러
    INVALID_PARAMETER(1000,HttpStatus.BAD_REQUEST, "파라미터 값이 없습니다!"),
    INTERNAL_SERVER_ERROR(1001,HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다!"),
    RESOURCE_NOT_FOUND(1002,HttpStatus.NOT_FOUND, "요청 url이 존재하지 않습니다!");

    //로그인 에러

    //그룹 에러


    //===================================
    private final int codenum;
    private final HttpStatus httpStatus;
    private final String message;
}
