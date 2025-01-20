package com.pro.domain.schedule.domain;

import com.pro.domain.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_id")
  private Long id;

  @NotBlank(message = "일정의 유저는 필수값입니다.")
  private Long user_id;

  @Nullable
  private Long group_schedule_id;

  private ColorEnum color;

  @NotBlank(message = "일정 시작시간은 필수값입니다.")
  private LocalDateTime start_time; //일정 시작시간 (yyyy-MM-ddTHH:mm:ss)

  @NotBlank(message = "일정 종료시간은 필수값입니다.")
  private LocalDateTime end_time; //일정 종료시간

  @NotBlank(message = "일정 제목은 필수값입니다.")
  private String title; //일정 제목
  
  @Nullable
  private String content; //일정 내용

  @Nullable
  private String place; //일정 장소

}
