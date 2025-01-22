package com.pro.domain.group.domain;

import com.pro.base.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotNull(message = "그룹 매니저의 email값은 필수값 입니다.")
  private String managerEmail; //id에서 email로 변경

  private LocalDate end_date; //그룹 끝 날짜 (유효 날짜)
}
