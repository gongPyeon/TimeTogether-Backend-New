package timetogeter.context.place.domain.repository.custom;

import timetogeter.context.place.application.dto.PlaceRatingDTO;

import java.util.List;

public interface PlaceBoardRepositoryCustom {
    List<PlaceRatingDTO> findAllRatingsByUserId(String userId);

    List<PlaceRatingDTO> findAllRatings();
}
