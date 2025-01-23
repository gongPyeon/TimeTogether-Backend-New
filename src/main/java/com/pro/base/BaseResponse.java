package com.pro.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pro.base.constant.BaseResponseCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpRequest;

import static com.pro.base.constant.BaseResponseCode.SUCCESS;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"success", "code", "message", "result", "api"})
public class BaseResponse<T> {

  @JsonProperty("success")
  private final boolean isSuccess;//요청 성공 여부
  private final int code;//BaseResponseCode 숫자코드
  @JsonProperty("message")
  private final String msg;//개발용: BaseResponseCode 숫자코드의 의미(메시지)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("result")
  private final T data;//반환 데이터
  private final String api;//요청 api

  public static <T> BaseResponse<T> of(BaseResponseCode code, String msg, T data, String apiPath) {
    return new BaseResponse<>(true, SUCCESS.getCode(), SUCCESS.getMsg(), data, apiPath);
  }

  public static <T> BaseResponse<T> of(BaseResponseCode code, T data, String apiPath) {
    return of(code, code.getMsg(), data, apiPath);
  }


}
