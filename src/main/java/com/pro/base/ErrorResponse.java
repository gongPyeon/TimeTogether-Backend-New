package com.pro.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pro.base.constant.BaseResponseCode;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"success", "code","message","api"})
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

  @JsonProperty("success")
  private final boolean isSuccess;
  private final int code;
  @JsonProperty("message")
  private final String msg;
  private final String api;


  /**
   * 에러
   * 
   * @param baseResponseCode
   */
  public ErrorResponse(BaseResponseCode baseResponseCode,String apiPath) {
    this.isSuccess = false;
    this.code = baseResponseCode.getCode();
    this.msg = baseResponseCode.getMsg();
    this.api = apiPath;
  }

}
