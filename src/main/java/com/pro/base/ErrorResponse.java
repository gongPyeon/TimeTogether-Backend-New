package com.pro.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pro.base.constant.BaseResponseCode;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

  @JsonProperty("success")
  private final boolean isSuccess;
  private final int code;
  private final HttpStatus status;
  private final String msg;


  /**
   * 에러
   * 
   * @param baseResponseCode
   */
  public ErrorResponse(BaseResponseCode baseResponseCode) {
    this.isSuccess = false;
    this.code = baseResponseCode.getCode();
    this.status = baseResponseCode.getStatus();
    this.msg = baseResponseCode.getMsg();
  }

}
