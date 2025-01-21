package com.pro.base.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {
  // 충돌 방지를 위한 Code format
  // 1(홀수)XXX: 이혜리
  // 2(짝수)XXX: 편강

  // ex)
  // USER_NICKNAME_DUPLICATED(13010, HttpStatus.BAD_REQUEST, "User nickname duplicated"),

  SUCCESS(1001, HttpStatus.OK, "Ok");

  private final int code;
  private final HttpStatus status;
  private final String msg;

}
