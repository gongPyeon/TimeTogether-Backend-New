package com.pro.domain.time.domain;

import com.pro.domain.date.domain.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_time")
public class Time {

  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "time_id")
  private Long id;

  @NotNull(message = "날짜(일) 아이디 값은 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "date_id")
  private Date date;

  @NotNull(message = "시간정보(24시)는 필수값입니다.")
  @Pattern(regexp = "^[01]{24}$", message = "시간은 24자리의 0 또는 1로만 구성되어야 합니다.")
  private String times;

  //Builder, of
  @Builder
  private Time(Date date, String times) {
    this.date = date;
    this.times = times;
  }

  public static Time of(Date date,  String times){
    return Time.builder()
            .date(date)
            .times(times)
            .build();
  }
}
