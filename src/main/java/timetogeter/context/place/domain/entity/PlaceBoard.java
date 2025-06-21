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
public class PlaceBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeBoardId;

    private String placeName;
    private String placeAddr;

    private Boolean aiPlace;

    public static PlaceBoard of(String placeName, String placeAddr, Boolean aiPlace) {
        PlaceBoard placeBoard = new PlaceBoard();
        placeBoard.placeName = placeName;
        placeBoard.placeAddr = placeAddr;
        placeBoard.aiPlace = aiPlace;
        return placeBoard;
    }

}
