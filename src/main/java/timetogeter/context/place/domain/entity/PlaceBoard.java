package timetogeter.context.place.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBoard { // TODO: 추가함 (약속이 확정됐을때 같이 추가함)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeBoardId;

    private String userId;
    private String placeName;
    private String placeAddr;
    private int placeRating;

    private double latitude;
    private double longitude;

    @ElementCollection
    private List<String> preferredCategories; // TODO: DDD에서 위험한지 확인
}
