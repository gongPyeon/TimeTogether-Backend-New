package timetogeter.global.interceptor.response.error.dto;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
public class ErrorResponse {
    private final int code;
    private final String message;
}
