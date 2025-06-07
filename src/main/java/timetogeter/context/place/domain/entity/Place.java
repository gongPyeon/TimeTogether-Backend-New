package timetogeter.context.place.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeId;

    private int promiseId;
    private String placeName;
    private String placeUrl;
    private String goal;
    private boolean isConfirmed;
    private int voting;

    private String userId; // 암호화된 사용자 고유 아이디

    public Place(int promiseId, String placeName, String placeUrl, String goal, String userId) {
        this.promiseId = promiseId;
        this.placeName = placeName; // TODO: 이름 조건 있는지 -> 1자 이상 30자 이내
        this.placeUrl = placeUrl; // TODO: 필수일지 선택일지, 여러개의 사진을 받을지 -> X
        this.goal = goal; // TODO: 목표 어떤 형식으로 어떻게 받을지 -> X
        this.isConfirmed = false;
        this.voting = 0; // TODO: 투표 최대값 설정 -> 인원소
        this.userId = userId;
    }

    public boolean hasVotedBy(String userId) {
        return this.userId == userId;
    }

    public void vote() {
        voting++;
    }

    public void cancelVote() {
        voting--;
    }
}
