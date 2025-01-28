package com.pro.domain.date.domain;

import com.pro.domain.meeting.domain.Meeting;
import com.pro.domain.time.domain.Time;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.sql.results.graph.Fetch;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_date")
public class Date {
  
  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "date_id")
  private Long id;

  @NotNull(message = "회의 아이디는 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  @NotNull(message = "일은 필수값입니다.")
  private LocalDate date;

  @NotNull(message = "요일값은 필수값입니다.")
  private String day;

  //연관관계 필드
  @OneToMany(mappedBy = "date")
  private List<Time> timeList = new ArrayList<>();

  //Builder, of
  @Builder
  private Date(Meeting meeting, LocalDate date, String day) {
    this.meeting = meeting;
    this.date = date;
    this.day = day;
  }

  public static Date of(Meeting meeting, LocalDate date, String day){
    return Date.builder()
            .meeting(meeting)
            .date(date)
            .day(day)
            .build();
  }

}
