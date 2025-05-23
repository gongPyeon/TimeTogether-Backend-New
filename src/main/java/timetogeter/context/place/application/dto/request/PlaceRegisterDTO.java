package timetogeter.context.place.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record PlaceRegisterDTO (@NotNull String placeName,
                                String placeUrl,
                                @NotNull String goal){ // TODO: 장소에 이미지도 받아? 오 목적도 받아?
}
