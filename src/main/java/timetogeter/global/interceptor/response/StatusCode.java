package timetogeter.global.interceptor.response;
import org.springframework.http.HttpStatus;


public interface StatusCode {
    int getCode();
    HttpStatus getHttpStatus();
    String getMessage();
}
