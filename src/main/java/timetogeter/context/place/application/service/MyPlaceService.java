package timetogeter.context.place.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.place.application.dto.PlaceRatingDTO;
import timetogeter.context.place.application.dto.request.AIReqDTO;
import timetogeter.context.place.application.dto.request.PlaceRegisterDTO;
import timetogeter.context.place.application.dto.request.UserAIInfoReqDTO;
import timetogeter.context.place.application.dto.response.PlaceRegisterResDTO;
import timetogeter.context.place.domain.entity.PromisePlace;
import timetogeter.context.place.domain.repository.PromisePlaceRepository;
import timetogeter.context.place.exception.InvalidPlaceNumException;
import timetogeter.context.place.exception.PlaceNotFoundException;
import timetogeter.context.place.exception.PlaceUserIdNotSame;
import timetogeter.context.place.infrastructure.external.AIPlaceClient;
import timetogeter.context.place.domain.repository.PlaceBoardRepository;
import timetogeter.context.promise.application.service.PromiseQueryService;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPlaceService { // TODO: 내 장소 관리 시스템

    private final PromisePlaceRepository placeRepository;
    private final PlaceBoardRepository placeBoardRepository;
    private final AIPlaceClient aiPlaceClient;
    private final PromiseQueryService promiseQueryService;

    public void deletePlace(String userId, int placeId) {
        PromisePlace place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(BaseErrorCode.PLACE_NOT_FOUND, "[ERROR] 아이디에 해당하는 장소를 찾을 수 없습니다."));

        if(!place.hasVotedBy(userId))
            throw new PlaceUserIdNotSame(BaseErrorCode.INVALID_USER, "[ERROR] 사용자에게 해당 장소를 삭제할 권한이 없습니다.");

        placeRepository.delete(place);
    }

    @Transactional
    public void registerPlace(String userId, String promiseId, List<PlaceRegisterDTO> dto) {
        if(dto.size() > 10) throw new InvalidPlaceNumException(BaseErrorCode.INVALID_PLACE_NUM, "[ERROR] 장소 등록 DTO의 사이즈가 10개를 넘습니다. 현재 "+dto.size()+"개 입니다.");
        List<PromisePlace> places = dto.stream()
                .map(p -> new PromisePlace(promiseId, p.placeName(), p.placeAddress(), p.placeInfo(), userId, p.aiPlace()))
                .collect(Collectors.toList());

        placeRepository.saveAll(places);
    }

    public List<PlaceRegisterResDTO> recommendPlace(String userId, String promiseId, UserAIInfoReqDTO dto) {
        List<PlaceRatingDTO> history = getByPlaceHistory(userId);
        String purpose = promiseQueryService.getPurpose(promiseId);
        AIReqDTO aiReqDTO = new AIReqDTO(userId, dto.latitude(), dto.longitude(), purpose, history);
        return aiPlaceClient.requestAIRecommendation(aiReqDTO);
    }

    private List<PlaceRatingDTO> getByPlaceHistory(String userId) {
        List<PlaceRatingDTO> history = placeBoardRepository.findAllRatingsByUserId(userId);

        return history;
    }
}
