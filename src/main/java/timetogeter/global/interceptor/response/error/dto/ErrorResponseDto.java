package timetogeter.global.interceptor.response.error.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponseDto {

    private final int codenum;
    private final String code;
    private final String message;
}
