package timetogeter.global.interceptor.response.error.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Response {
    private final int code;
    private final String message;
}
