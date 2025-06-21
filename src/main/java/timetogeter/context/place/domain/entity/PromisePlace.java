package timetogeter.context.place.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogeter.context.place.exception.InvalidPlaceInfoException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromisePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeId;

    private String promiseId;
    private String userId;

    private String placeName;
    private String placeAddr;
    private String placeInfo;

    private int voting;
    private Boolean aiPlace;

    public PromisePlace(String promiseId, String placeName, String placeAddr, String placeInfo, String userId, boolean aiPlace) {
        validatePlace(placeName, placeAddr, placeInfo);
        this.promiseId = promiseId;
        this.placeAddr = placeAddr;
        this.placeName = placeName;
        this.placeInfo = placeInfo;
        this.voting = 0;
        this.userId = userId;
        this.aiPlace = aiPlace;
    }

    private void validatePlace(String placeName, String placeAddr, String placeInfo) {
        validateNull(placeName, placeAddr);
        validatePlaceName(placeName);
        validatePlaceInfo(placeInfo);
    }

    private void validatePlaceInfo(String placeInfo) {
        if(placeInfo == null) return;
        if(placeInfo.length() > 200)
            throw new InvalidPlaceInfoException(BaseErrorCode.INVALID_PLACE_INFO, "[ERROR] 장소 정보는 200자 이내만 가능합니다.");

    }

    private void validatePlaceName(String placeName) {
        if(placeName.length() < 1 || placeName.length() > 30)
            throw new InvalidPlaceInfoException(BaseErrorCode.INVALID_PLACE_NAME, "[ERROR] 장소 이름은 1자 이상 30자 이내만 가능합니다.");
    }

    private void validateNull(String placeName, String placeAddr) {
        if(placeName == null || placeAddr == null)
            throw new InvalidPlaceInfoException(BaseErrorCode.PLACE_NULL, "[ERROR] 장소 등록 필수 정보가 누락됐습니다.");
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
}
