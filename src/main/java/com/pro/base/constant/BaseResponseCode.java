package com.pro.base.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseResponseCode {
  // 충돌 방지를 위한 Code format
  // 1(홀수)XXX: 이혜리
  // 2(짝수)XXX: 편강

  SUCCESS(2001, HttpStatus.OK, "OK");

  private final int code;
  private final HttpStatus status;
  private final String msg;

}
