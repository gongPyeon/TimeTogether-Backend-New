package timetogeter.context.place.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import timetogeter.context.place.application.dto.PlaceDTO;
import timetogeter.context.place.application.dto.response.PlaceBoardDTO;
import timetogeter.context.place.domain.entity.Place;
import timetogeter.context.place.domain.repository.PlaceRepository;
import timetogeter.context.place.exception.PlaceNotFoundException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static timetogeter.global.common.util.PageUtil.PLACE_PAGE;

@Service
@RequiredArgsConstructor
public class PlaceBoardService { // TODO: 장소 관리 시스템

    private final PlaceRepository placeRepository;
    private final VotingSystem votingSystem;

    public PlaceBoardDTO getPlaceBoard(String userId, int promiseId, int page) {
        PageRequest pageRequest = PageRequest.of(page, PLACE_PAGE);
        Page<Place> placePage = placeRepository.findByPromiseId(promiseId, pageRequest);

        List<PlaceDTO> places = placePage.getContent()
                .stream()
                .map(p -> new PlaceDTO(p.getPlaceId(), p.getPlaceName(), p.getPlaceUrl(), p.getVoting(),
                        p.hasVotedBy(userId), votingSystem.hasVotedBy(userId, p.getPlaceId())))
                .collect(Collectors.toList());

        return new PlaceBoardDTO(page, placePage.getTotalPages(), places);
    }

    // TODO: Vote와 Place를 분리할지 고민
    @Transactional
    public void vote(String userId, int placeId) {
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(BaseErrorCode.PLACE_NOT_FOUND, "[ERROR] 아이디에 해당하는 장소를 찾을 수 없습니다."));
        place.vote();
        votingSystem.vote(userId, placeId);

        placeRepository.save(place);
    }

    public void deleteVote(String userId, int placeId) {
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(BaseErrorCode.PLACE_NOT_FOUND, "[ERROR] 아이디에 해당하는 장소를 찾을 수 없습니다."));
        place.cancelVote();
        votingSystem.cancelVote(userId, placeId);

        placeRepository.save(place);
    }
}
