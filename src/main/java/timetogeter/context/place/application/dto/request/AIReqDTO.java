package timetogeter.context.place.application.dto.request;

import timetogeter.context.place.application.dto.PlaceRatingDTO;

import java.util.List;

public record AIReqDTO (String userId,
                        double latitude,
                        double longitude,
                        String purpose,
                        List<PlaceRatingDTO> history)
{}
