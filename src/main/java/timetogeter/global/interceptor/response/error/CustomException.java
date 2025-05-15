package timetogeter.global.interceptor.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

//해당 클래스를 context 마다 하나씩 만들어서 GlobalExceptionHandler에서 사용!
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    public BaseErrorCode status;
    public String message;
}
