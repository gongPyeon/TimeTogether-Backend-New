package timetogeter.context.place.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record PlaceRegisterDTO (@NotNull String placeName,
                                @NotNull String placeAddress,
                                String placeId,
                                String placeInfo,
                                boolean aiPlace){
}
