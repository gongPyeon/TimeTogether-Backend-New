package com.pro.domain.group_user_middle.domain;

import com.pro.base.common.BaseEntity;
import com.pro.domain.group.domain.Group;
import com.pro.oauth2.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_group_user_middle")
public class GroupUserMiddle extends BaseEntity {

  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_user_middle_id")
  private Long id;

  @NotNull(message = "사용자는 필수입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull(message = "그룹은 필수입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  //Builder, of
  @Builder
  private GroupUserMiddle(User user, Group group) {
    this.user = user;
    this.group = group;
  }

  public static GroupUserMiddle of(User user, Group group){
    return GroupUserMiddle.builder()
            .user(user)
            .group(group)
            .build();
  }
}
