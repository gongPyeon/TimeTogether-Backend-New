package timetogeter.context.place.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.place.domain.entity.Place;
import timetogeter.context.place.domain.repository.PlaceRepository;
import timetogeter.context.place.exception.PlaceNotFoundException;
import timetogeter.context.place.exception.PlaceUserIdNotSame;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Service
@RequiredArgsConstructor
public class MyPlaceService { // TODO: 내 장소 관리 시스템

    private final PlaceRepository placeRepository;
    public void deletePlace(String userId, int placeId) {
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(BaseErrorCode.PLACE_NOT_FOUND, "[ERROR] 아이디에 해당하는 장소를 찾을 수 없습니다."));

        if(!place.hasId(userId))
            throw new PlaceUserIdNotSame(BaseErrorCode.INVALID_USER, "[ERROR] 사용자에게 해당 장소를 삭제할 권한이 없습니다.");

        placeRepository.delete(place);
    }
}
