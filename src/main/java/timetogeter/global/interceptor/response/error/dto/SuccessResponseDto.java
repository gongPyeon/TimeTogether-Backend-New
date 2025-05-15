package timetogeter.global.interceptor.response.error.dto;

import lombok.Getter;
import timetogeter.global.interceptor.response.error.GlobalErrorCode;

@Getter
public class SuccessResponseDto<T> extends ResponseDto {
    private final T data;

    private SuccessResponseDto(T data){
        super(GlobalErrorCode.OK.getCodenum(), GlobalErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> SuccessResponseDto<T> from(T data){
        return new SuccessResponseDto<>(data);
    }
}
