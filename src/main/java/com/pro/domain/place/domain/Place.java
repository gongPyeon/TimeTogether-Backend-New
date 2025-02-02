package com.pro.domain.place.domain;

import com.pro.base.common.BaseEntity;
import com.pro.domain.meeting.domain.Meeting;
import com.pro.domain.vote.domain.Vote;
import jakarta.annotation.Nullable;
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
@Table(name = "t_mn_place")
public class Place{

  //자체 필드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "place_id")
  private Long id;

  @NotNull(message = "회의 아이디 값은 필수값입니다.")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  @NotNull(message = "장소 이름은 필수값입니다.")
  @Column(length = 150)
  private String placeName;

  @Nullable
  private String placeUrl;

  @NotNull(message = "장소 확정 유무는 필수값입니다.")
  private boolean placeComplete;

  //연관관계 필드
  @OneToMany(mappedBy = "place")
  private List<Vote> voteList = new ArrayList<>();

  //Builder, of
  @Builder
  private Place(Meeting meeting, String placeName, @Nullable String placeUrl, boolean placeComplete) {
    this.meeting = meeting;
    this.placeName = placeName;
    this.placeUrl = placeUrl;
    this.placeComplete = placeComplete;
  }

  public static Place of(Meeting meeting, String placeName,  @Nullable String placeUrl, boolean placeComplete){
    return Place.builder()
            .meeting(meeting)
            .placeName(placeName)
            .placeUrl(placeUrl)
            .placeComplete(placeComplete)
            .build();
  }
}
