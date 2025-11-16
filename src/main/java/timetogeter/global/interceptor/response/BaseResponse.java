package timetogeter.global.interceptor.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BaseResponse<T> {

    @Schema(description = "결과 코드", example = "200")
    private int code = BaseCode.OK.getCode(); // 필요할지 고민
    @Schema(description = "메시지", example = "요청에 성공했습니다.")
    private String message = BaseCode.OK.getMessage(); // 필요할지 고민
    @Schema(description = "응답 데이터")
    private T result;

    public BaseResponse(T result) {
        this.result = result;
    }

    public BaseResponse(BaseCode baseCode) {
        this.code = baseCode.getCode();
        this.message = baseCode.getMessage();
    }

    public BaseResponse(T result, BaseCode baseCode) {
        this.code = baseCode.getCode();
        this.message = baseCode.getMessage();
        this.result = result;
    }
}
