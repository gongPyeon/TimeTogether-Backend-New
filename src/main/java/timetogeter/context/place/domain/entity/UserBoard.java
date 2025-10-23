package timetogeter.context.place.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogeter.context.place.exception.PlaceNotFoundException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userBoardId;
    private int placeBoardId;
    private String userId;

    private int rating;

    public UserBoard(int placeBoardId, String userId, int rating) {
        validRating(rating);
        this.placeBoardId = placeBoardId;
        this.userId = userId;
        this.rating = rating;
    }

    public static UserBoard of(int placeBoardId, String userId, int rating) {
        return new UserBoard(placeBoardId, userId, rating);
    }

    public void update(int placeBoardId, String userId, int rating) {
        validRating(rating);
        this.placeBoardId = placeBoardId;
        this.userId = userId;
        this.rating = rating;
    }

    public void validRating(int rating){
        if(rating < 0 || 5 < rating)
            throw new PlaceNotFoundException(BaseErrorCode.PLACE_RATING_NUM, "[ERROR] 장소 평점 범위는 1~5입니다.");
    }
}