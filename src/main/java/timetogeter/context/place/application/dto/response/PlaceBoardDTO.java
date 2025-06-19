package timetogeter.context.place.application.dto.response;

import timetogeter.context.place.application.dto.PlaceDTO;

import java.util.List;

public record PlaceBoardDTO (int page,
                             int total,
                             List<PlaceDTO> places){
}
