package timetogeter.context.place.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.place.application.dto.PlaceDTO;
import timetogeter.context.place.application.dto.request.PlaceRegisterDTO;
import timetogeter.context.place.domain.entity.Place;
import timetogeter.context.place.domain.repository.PlaceRepository;
import timetogeter.context.place.exception.InvalidPlaceNumException;
import timetogeter.context.place.exception.PlaceNotFoundException;
import timetogeter.context.place.exception.PlaceUserIdNotSame;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPlaceService { // TODO: 내 장소 관리 시스템

    private final PlaceRepository placeRepository;
    private final AIPlaceClient aiPlaceClient;

    public void deletePlace(String userId, int placeId) {
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(BaseErrorCode.PLACE_NOT_FOUND, "[ERROR] 아이디에 해당하는 장소를 찾을 수 없습니다."));

        if(!place.hasVotedBy(userId))
            throw new PlaceUserIdNotSame(BaseErrorCode.INVALID_USER, "[ERROR] 사용자에게 해당 장소를 삭제할 권한이 없습니다.");

        placeRepository.delete(place);
    }

    @Transactional
    public void registerPlace(String userId, int promiseId, List<PlaceRegisterDTO> dto) {
        if(dto.size() > 10) throw new InvalidPlaceNumException(BaseErrorCode.INVALID_PLACE_NUM, "[ERROR] DTO의 사이즈가 10개를 넘습니다. 현재 "+dto.size()+"개 입니다.");
        List<Place> places = dto.stream()
                .map(p -> new Place(promiseId, p.placeName(), p.placeUrl(), p.goal(), userId))
                .collect(Collectors.toList());

        placeRepository.saveAll(places);
    }

    public void recommendPlace(String userId, int promiseId) {

    }
}
