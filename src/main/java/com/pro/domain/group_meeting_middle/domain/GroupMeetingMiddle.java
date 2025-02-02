package com.pro.domain.group_meeting_middle.domain;

import com.pro.base.common.BaseEntity;
import com.pro.domain.date.domain.Date;
import com.pro.domain.group.domain.Group;
import com.pro.domain.meeting.domain.Meeting;
import com.pro.oauth2.entity.User;
import jakarta.persistence.*;
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
@Table(name = "t_mn_group_meeting_middle")
public class GroupMeetingMiddle extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_meeting_middle_id")
  private Long id;

  @NotNull(message = "그룹은 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @NotNull(message = "회의는 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  @NotNull(message = "사용자는 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull(message = "회의 확정 유무는 필수값입니다.")
  private boolean meetComplete;

  //연관관계 필드
  @OneToMany(mappedBy = "groupMeetingMiddle")
  private List<Date> dateList = new ArrayList<>();

  //Builder, of
  @Builder
  private GroupMeetingMiddle(Group group, Meeting meeting, User user, boolean meetComplete) {
    this.group = group;
    this.meeting = meeting;
    this.user = user;
    this.meetComplete = meetComplete;
  }

  public static GroupMeetingMiddle of(Group group, Meeting meeting,  User user, boolean meetComplete){
    return GroupMeetingMiddle.builder()
            .group(group)
            .meeting(meeting)
            .user(user)
            .meetComplete(meetComplete)
            .build();
  }
}