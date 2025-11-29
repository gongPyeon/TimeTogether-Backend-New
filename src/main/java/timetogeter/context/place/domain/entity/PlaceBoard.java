package timetogeter.context.place.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert; // JPA 엔티티에 기본값을 적용할 때 유용

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class PlaceBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeBoardId;

    private Boolean aiPlace;

    private String placeAddr;

    private String placeName;

    private String score;

    private Double userScore;

    private Integer userCount;

    private String categories;

    @Column(columnDefinition = "DECIMAL(11,8)")
    private Double x;

    @Column(columnDefinition = "DECIMAL(11,8)")
    private Double y;

    private String phone;

    private String hashtags;

    private String url;

    private String menu;

    private String placeInfo;

    public static PlaceBoard of(String placeName, String placeAddr, Boolean aiPlace, String placeInfo) {
        PlaceBoard placeBoard = new PlaceBoard();
        placeBoard.placeName = placeName;
        placeBoard.placeAddr = placeAddr;
        placeBoard.aiPlace = aiPlace;
        placeBoard.placeInfo = placeInfo;

        return placeBoard;
    }
}