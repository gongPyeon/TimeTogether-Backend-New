package com.pro.domain.meeting.domain;

import com.pro.domain.date.domain.Date;
import com.pro.domain.group.domain.Group;
import com.pro.domain.group_meeting_middle.domain.GroupMeetingMiddle;
import com.pro.domain.place.domain.Place;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_meeting")
public class Meeting {

  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meeting_id")
  private Long id;

  @NotBlank(message = "회의 제목은 필수값입니다.")
  private String title; //회의 제목

  @NotNull(message = "회의 유형은 필수값입니다.")
  private MeetingType meetingType; //회의 유형

  @Nullable
  private String intro; //회의 소제목

  @NotNull(message = "그룹 매니저의 email값은 필수값입니다.")
  private String managerEmail;

  @NotBlank(message = "회의날짜(기간)값은 필수값입니다.")
  private String dates;

  @NotBlank(message = "회의시간범위는 필수값입니다.")
  private String timeRange; //회의시간범위

  @NotNull(message = "진행되는 회의시간은 필수값입니다.")
  @Digits(integer = 2, fraction = 2)
  @Max(24) @Min(0)
  private BigDecimal time; //진행되는 회의시간

  @NotNull(message = "그룹은 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  //연관관계 필드
  @OneToMany(mappedBy = "meeting")
  private List<GroupMeetingMiddle> groupMeetingMiddleList = new ArrayList<>();

  @OneToMany(mappedBy = "meeting")
  private List<Place> placeList = new ArrayList<>();

  //Builder, of
  @Builder
  private Meeting(String title, MeetingType meetingType, @Nullable String intro, String managerEmail, String dates, String timeRange, BigDecimal time,Group group) {
    this.title = title;
    this.meetingType = meetingType;
    this.intro = intro;
    this.managerEmail = managerEmail;
    this.dates = dates;
    this.timeRange = timeRange;
    this.time = time;
    this.group = group;
  }

  public static Meeting of(String title, MeetingType meetingType, @Nullable String intro, String managerEmail, String dates, String timeRange, BigDecimal time,Group group) {
    return Meeting.builder()
            .title(title)
            .meetingType(meetingType)
            .intro(intro)
            .managerEmail(managerEmail)
            .dates(dates)
            .timeRange(timeRange)
            .time(time)
            .group(group)
            .build();
  }

}