package com.pro.domain.meeting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_meeting")
public class Meeting {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meeting_id")
  private Long id;

  @NotBlank(message = "그룹 아이디는 필수값 입니다.")
  private Long group_id;

  @NotBlank(message = "회의 제목은 필수값 입니다.")
  private String title; //회의 제목

  private MeetingType meetingType; //회의 유형

  private String intro; //회의 소제목

  @NotBlank(message = "회의 매니저는 필수값 입니다.")
  private Long manager_id;

  @NotBlank(message = "회의날짜 ")
  private String dates; //회의날짜(기간) -- 수정할 것

  @NotBlank(message = "회의시간범위는 필수값 입니다.")
  private String time_range; //회의시간범위

  @NotBlank(message = "진행되는 회의시간은 필수값 입니다.")
  @Digits(integer = 2, fraction = 2)
  @Max(24) @Min(0)
  private float time; //진행되는 회의시간

}