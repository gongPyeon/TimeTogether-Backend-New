package timetogeter.global.interceptor.response.error.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timetogeter.global.interceptor.response.error.errorbase.ErrorCode;

//해당 클래스를 context 마다 하나씩 만들어서 GlobalExceptionHandler에서 사용!
@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

}
