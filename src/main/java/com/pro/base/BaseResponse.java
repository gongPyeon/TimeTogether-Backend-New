package com.pro.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pro.base.constant.Code;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.pro.base.constant.Code.SUCCESS;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess", "code","message","result"})
public class BaseResponse<T> {

  @JsonProperty("isSuccess")
  private final boolean isSuccess;
  private final int code;
  @JsonProperty("message")
  private final String msg;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final T data;


  /**
   * 정보를 요청한 경우
   * @param data
   */
  public BaseResponse(T data) {
    this.isSuccess = true;
    this.code = SUCCESS.getCode();
    this.msg = SUCCESS.getMsg();
    this.data = data;
  }

  /**
   * 응답이 예/아니오/오류 인 경우
   * @param code
   */
  public BaseResponse(Code code) {
    this.isSuccess = true;
    this.code = code.getCode();
    this.msg = code.getMsg();
    this.data = null;
  }
}
