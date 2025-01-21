package com.pro.domain.group_schedule_info.domain;

import com.pro.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_group_schedule_info")
public class Group_schedule_info extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_schedule_id")
  private Long id;

  @NotNull(message = "그룹 아이디는 필수값 입니다.")
  private Long group_id;
}