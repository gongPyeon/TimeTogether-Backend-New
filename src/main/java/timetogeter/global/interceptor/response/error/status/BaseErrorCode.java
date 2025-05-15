package timetogeter.global.interceptor.response.error.status;

import lombok.*;
import org.springframework.http.HttpStatus;

//필요한 에러를 //[에러이름] 에러 란에 만들기
@Getter
@RequiredArgsConstructor
public enum BaseErrorCode implements StatusCode {
    //공통 에러
    INVALID_PARAMETER(1000,HttpStatus.BAD_REQUEST, "파라미터 값이 없습니다!"),
    INTERNAL_SERVER_ERROR(1001,HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다!"),
    RESOURCE_NOT_FOUND(1002,HttpStatus.NOT_FOUND, "요청 url이 존재하지 않습니다!"),

    //인증&인가 에러
    INVALID_LOGIN(400, HttpStatus.BAD_REQUEST, "예외처리 아이디 또는 비밀번호를 확인해주세요."),
    INVALID_AUTH(500, HttpStatus.INTERNAL_SERVER_ERROR, "인증 처리를 실패했어요"),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않아요"),
    INVALID_USER(401, HttpStatus.UNAUTHORIZED, "접근 권한이 없어요");

    //로그인 에러

    //그룹 에러


    //===================================
    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
