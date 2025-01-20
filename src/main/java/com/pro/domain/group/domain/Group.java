package com.pro.domain.group.domain;

import com.pro.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_group")
public class Group extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Long id;

  @NotBlank(message = "그룹 이름은 필수값 입니다.")
  private String name;
  private String img;

  @NotBlank(message = "그룹 매니저는 필수값 입니다.")
  private Long manager_id;

  private LocalDate end_date; //그룹 끝 날짜 (유효 날짜)
}
