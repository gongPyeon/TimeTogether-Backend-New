package timetogeter.context.place.application.dto;

public record PlaceDTO (int id,
                        String placeName,
                        String placeAddr,
                        int voting,
                        boolean isRemoved,
                        boolean voted,
                        int aiPlace){
}
