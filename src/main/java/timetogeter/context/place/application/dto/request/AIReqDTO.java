package timetogeter.context.place.application.dto.request;

import timetogeter.context.place.application.dto.PlaceRatingDTO;

import java.util.List;

public record AIReqDTO (String userId,
                        double latitude,
                        double longitude,
                        List<String> preferredCategories,
                        List<PlaceRatingDTO> placeRatingDTOList) // 히스토리 데이터 (장소이름, 장소에 매긴 평점)
{}
