package com.pro.domain.date.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_date")
public class Date {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "date_id")
  private Long id;

  @NotBlank(message = "회의 아이디는 필수값 입니다.")
  private Long meeting_id;

  @NotBlank(message = "날짜는 필수값 입니다.")
  private LocalDate date;

  @Pattern(regexp = "^[01]+$", message ="day는 0과 1로만 이루어져야 합니다.")
  private String day;
}
