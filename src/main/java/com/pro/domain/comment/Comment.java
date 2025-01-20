package com.pro.domain.comment;

import com.pro.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  @NotBlank(message = "일정 아이디 값은 필수입니다.")
  private Long schedule_id;

  @NotBlank
  @Column(columnDefinition = "TEXT")//65535자의 고정된 최대 크기
  private String content; //댓글내용

}
