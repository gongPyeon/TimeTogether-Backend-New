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

    private String promiseId;
    private String placeName;
    private String placeAddr; // 주소
    private String placeUrl;
    private String placeInfo; // goal >> placeInfo
    private boolean isConfirmed;
    private int voting;

    private String userId; // 암호화된 사용자 고유 아이디

    public Place(String promiseId, String placeName, String placeAddr, String placeUrl, String placeInfo, String userId) {
        this.promiseId = promiseId;
        this.placeAddr = placeAddr;
        this.placeName = placeName; // TODO: 이름 조건 있는지 -> 1자 이상 30자 이내
        this.placeUrl = placeUrl; // TODO: 필수일지 선택일지, 여러개의 사진을 받을지 -> X
        this.placeInfo = placeInfo; // TODO: 목표 어떤 형식으로 어떻게 받을지 -> X
        this.isConfirmed = false;
        this.voting = 0; // TODO: 투표 최대값 설정 -> 인원수
        this.userId = userId;
    }

    public boolean hasVotedBy(String userId) {
        return this.userId != null && this.userId.equals(userId);
    }

    public void vote() {
        voting++;
    }

    public void cancelVote() {
        voting--;
    }

    public void confirm() {
        this.isConfirmed = true;
    }

    public void revokeConfirmation() {
        this.isConfirmed = false;
    }
}
