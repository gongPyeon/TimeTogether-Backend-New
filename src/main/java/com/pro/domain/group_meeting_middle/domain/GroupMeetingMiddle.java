package com.pro.domain.group_meeting_middle.domain;

import com.pro.base.common.BaseEntity;
import com.pro.domain.group.domain.Group;
import com.pro.domain.meeting.domain.Meeting;
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
@Table(name = "t_mn_group_meeting_middle")
public class GroupMeetingMiddle extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_meeting_middle_id")
  private Long id;

  @NotNull(message = "사용자는 필수입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @NotNull(message = "그룹은 필수입니다.")
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  @NotNull(message = "회의 확정 유무는 필수값입니다.")
  private boolean meetComplete;

  //Builder, of
  @Builder
  private GroupMeetingMiddle(Group group, Meeting meeting, boolean meetComplete) {
    this.group = group;
    this.meeting = meeting;
    this.meetComplete = meetComplete;
  }

  public static GroupMeetingMiddle of(Group group, Meeting meeting, boolean meetComplete){
    return GroupMeetingMiddle.builder()
            .group(group)
            .meeting(meeting)
            .meetComplete(meetComplete)
            .build();
  }
}