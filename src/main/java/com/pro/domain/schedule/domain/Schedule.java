package com.pro.domain.schedule.domain;

import com.pro.base.common.BaseEntity;
import com.pro.domain.comment.domain.Comment;
import com.pro.oauth2.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_schedule")
public class Schedule extends BaseEntity {

  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_id")
  private Long id;

  @NotNull(message = "일정의 유저는 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="user_id")
  private User user;

  @Nullable
  private Long group_schedule_id;

  @Nullable
  private ColorEnum color;

  @NotNull(message = "일정 시작시간은 필수값입니다.")
  private LocalDateTime start_time; //일정 시작시간 (yyyy-MM-ddTHH:mm:ss)

  @NotNull(message = "일정 종료시간은 필수값입니다.")
  private LocalDateTime end_time; //일정 종료시간

  @NotBlank(message = "일정 제목은 필수값입니다.")
  private String title; //일정 제목
  
  @Nullable
  private String content; //일정 내용

  @Nullable
  private String place_name; //일정 장소 이름

  @Nullable
  private String place_url; //일정 장소 url

  //연관관계 필드
  @OneToMany(mappedBy = "schedule")
  private List<Comment> comments = new ArrayList<>();

  //Builder, of
  @Builder
  private Schedule(User user, @Nullable Long group_schedule_id, ColorEnum color, LocalDateTime start_time, LocalDateTime end_time, String title, @Nullable String content, @Nullable String place_name, @Nullable String place_url) {
    this.user = user;
    this.group_schedule_id = group_schedule_id;
    this.color = color;
    this.start_time = start_time;
    this.end_time = end_time;
    this.title = title;
    this.content = content;
    this.place_name = place_name;
    this.place_url = place_url;
  }

  public static Schedule of (User user, @Nullable Long group_schedule_id, ColorEnum color, LocalDateTime start_time, LocalDateTime end_time, String title, @Nullable String content, @Nullable String place_name, @Nullable String place_url){
    return Schedule.builder()
            .user(user)
            .group_schedule_id(group_schedule_id)
            .color(color)
            .start_time(start_time)
            .end_time(end_time)
            .title(title)
            .content(content)
            .place_name(place_name)
            .place_url(place_url)
            .build();
  }


}
