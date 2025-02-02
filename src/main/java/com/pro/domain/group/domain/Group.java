package com.pro.domain.group.domain;

import com.pro.base.common.BaseEntity;
import com.pro.domain.group_meeting_middle.domain.GroupMeetingMiddle;
import com.pro.domain.group_user_middle.domain.GroupUserMiddle;
import com.pro.domain.meeting.domain.Meeting;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_group")
public class Group extends BaseEntity {

  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Long id;

  @NotBlank(message = "그룹 이름은 필수값 입니다.")
  private String name;

  @Nullable
  private String img;

  @NotNull(message = "그룹 매니저의 email값은 필수값 입니다.")
  private String managerEmail;

  //연관관계 필드
  @OneToMany(mappedBy = "group")
  private List<GroupUserMiddle> groupUserMiddleList = new ArrayList<>();

  @OneToMany(mappedBy = "group")
  private List<Meeting> meetingList = new ArrayList<>();

  //Builder, of
  @Builder
  public Group(String name, @Nullable String img, String managerEmail) {
    this.name = name;
    this.img = img;
    this.managerEmail = managerEmail;
  }

  public static Group of(String name, @Nullable String img, String managerEmail){
    return Group.builder()
            .name(name)
            .img(img)
            .managerEmail(managerEmail)
            .build();
  }
}