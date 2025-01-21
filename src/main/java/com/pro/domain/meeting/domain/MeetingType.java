package com.pro.domain.meeting.domain;

import lombok.Getter;

@Getter
public enum MeetingType {

  ONLINE("온라인"),
  OFFLINE("오프라인");

  private final String meetType;

  MeetingType(String meetType) {
    this.meetType = meetType;
  }
}
