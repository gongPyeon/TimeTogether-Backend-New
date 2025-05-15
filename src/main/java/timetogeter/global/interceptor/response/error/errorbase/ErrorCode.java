package timetogeter.global.interceptor.response.error.errorbase;
import org.springframework.http.HttpStatus;


public interface ErrorCode {
    String name();
    int getCodenum();
    HttpStatus getHttpStatus();
    String getMessage();
}
