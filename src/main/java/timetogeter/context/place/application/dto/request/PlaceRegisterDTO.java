package timetogeter.context.place.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record PlaceRegisterDTO (@NotNull String placeName,
                                String placeAddress,
                                String placeInfo,
                                boolean aiPlace){
}
