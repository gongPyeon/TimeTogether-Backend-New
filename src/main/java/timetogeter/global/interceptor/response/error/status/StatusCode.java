package timetogeter.global.interceptor.response.error.status;
import org.springframework.http.HttpStatus;


public interface StatusCode {
    String name();
    int getCode();
    HttpStatus getHttpStatus();
    String getMessage();
}
