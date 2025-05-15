package timetogeter.global.interceptor.response.error.dto;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder // 이거의 필요성이 있나?
@RequiredArgsConstructor
public class ErrorResponse {
    private final String name;
    private final int code;
    private final String message;
}
