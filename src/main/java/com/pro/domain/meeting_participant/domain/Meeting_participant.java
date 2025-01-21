package com.pro.domain.meeting_participant.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "t_mn_meeting_participant")
public class Meeting_participant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meeting_participant_id")
  private Long id;

  @NotNull(message = "회의 아이디는 필수값 입니다.")
  private Long meeting_id;

  @NotNull(message = "사용자 아이디는 필수값 입니다.")
  private Long user_id;

  @CreationTimestamp
  private LocalDateTime joined_at; //참여일시
}
