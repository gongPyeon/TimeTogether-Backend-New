package timetogeter.context.promise.domain.entity;

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
public class PromiseCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promiseCheckId;

    private String promiseId;
    private String dateTime;
    private int placeId;

    public PromiseCheck(String promiseId, String dateTime) {
        this.promiseId = promiseId;
        this.dateTime = dateTime;
    }

    public PromiseCheck(String promiseId, int placeId) {
        this.promiseId = promiseId;
        this.placeId = placeId;
    }

    public void setPlaceId(int placeId){
        this.placeId = placeId;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

}
