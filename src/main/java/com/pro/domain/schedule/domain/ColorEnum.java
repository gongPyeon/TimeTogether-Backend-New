package com.pro.domain.schedule.domain;

import lombok.Getter;

@Getter
public enum ColorEnum {
  RED("빨강"),
  BLUE("파랑"),
  YELLOW("노랑"),
  NAVY("네이비"),
  BEIGE("베이지"),
  WHILE("하양"),
  BLACK("검정"),
  CYAN("청록"),
  CORAL("코랄");

  private final String colorName;

  ColorEnum(String colorName) {
    this.colorName = colorName;
  }
}
