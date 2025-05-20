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
    private boolean isConfirmed;
    private int voting;

    private String userId; // 암호화된 사용자 고유 아이디

    public boolean hasId(String userId) {
        return this.userId == userId;
    }
}
