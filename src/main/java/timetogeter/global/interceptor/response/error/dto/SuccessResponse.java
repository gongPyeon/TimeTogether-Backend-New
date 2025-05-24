package timetogeter.global.interceptor.response.error.dto;

import lombok.Getter;
import timetogeter.global.interceptor.response.BaseCode;

@Getter
public class SuccessResponse<T> extends Response {
    private final T data;

    private SuccessResponse(T data){
        super(BaseCode.OK.getCode(), BaseCode.OK.getMessage());
        this.data = data;
    }

    public static <T> SuccessResponse<T> from(T data){
        return new SuccessResponse<>(data);
    }
}
