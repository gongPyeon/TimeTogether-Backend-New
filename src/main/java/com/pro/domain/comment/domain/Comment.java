package com.pro.domain.comment.domain;

import com.pro.base.common.BaseEntity;
import com.pro.domain.schedule.domain.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_comment")
public class Comment extends BaseEntity {

  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  @NotNull(message = "일정 아이디 값은 필수입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  @NotBlank
  @Column(columnDefinition = "TEXT")//65535자의 고정된 최대 크기
  private String content; //댓글내용

  //Builder, of
  @Builder
  private Comment(Schedule schedule, String content) {
    this.schedule = schedule;
    this.content = content;
  }

  public static Comment of(Schedule schedule, String content){
    return Comment.builder()
            .schedule(schedule)
            .content(content)
            .build();
  }
}
