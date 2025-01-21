package com.pro.base;

import com.pro.base.constant.Code;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

  private final boolean isSuccess;
  private final int code;
  private final HttpStatus status;
  private final String msg;


  /**
   * 에러
   * 
   * @param code
   */
  public ErrorResponse(Code code) {
    this.isSuccess = false;
    this.code = code.getCode();
    this.status = code.getStatus();
    this.msg = code.getMsg();
  }

}
