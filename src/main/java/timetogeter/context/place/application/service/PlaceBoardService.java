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
import timetogeter.context.vote.application.service.VotingService;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.List;
import java.util.stream.Collectors;

import static timetogeter.global.common.util.PageUtil.PLACE_PAGE;

@Service
@RequiredArgsConstructor
public class PlaceBoardService { // TODO: 장소 관리 시스템

    private final PlaceRepository placeRepository;
    private final VotingService votingService; // place <= vote 의존

    public PlaceBoardDTO getPlaceBoard(String userId, int promiseId, int page) {
        PageRequest pageRequest = PageRequest.of(page, PLACE_PAGE);
        Page<Place> placePage = placeRepository.findByPromiseId(promiseId, pageRequest);

        List<PlaceDTO> places = placePage.getContent()
                .stream()
                .map(p -> new PlaceDTO(p.getPlaceId(), p.getPlaceName(), p.getPlaceUrl(), p.getVoting(),
                        p.hasVotedBy(userId),  // 투표 유무
                        votingService.hasVotedBy(userId, p.getPlaceId()))) // 투표 취소가 가능한지
                .collect(Collectors.toList());

        return new PlaceBoardDTO(page, placePage.getTotalPages(), places);
    }

    @Transactional
    public void vote(String userId, int placeId) {
        votingService.vote(userId, placeId);

        Place place = get(placeId);
        place.vote();
        placeRepository.save(place);
    }

    public void deleteVote(String userId, int placeId) {
        votingService.cancelVote(userId, placeId);

        Place place = get(placeId);
        placeRepository.save(place);
    }

    public void confirmedPlace(String userId, int placeId) {
        // TODO: Promise Service에서 약속장 확인

        // TODO: Promise Service에 확정된 Place DTO 넘기기 (장소이름, 장소유형, 장소URL) - 목표까지 필요한지 확인

    }

    public void reConfirmedPlace(String userId, int placeId) {
        // TODO: Promise Service에서 약속장 확인

        // TODO: Promise Service에 확정된 Place DTO 넘기기 - 기존에 있는 Schedule을 가져와서 수정해서 반영

    }

    private Place get(int placeId){
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(BaseErrorCode.PLACE_NOT_FOUND, "[ERROR] 아이디에 해당하는 장소를 찾을 수 없습니다."));
        return place;
    }
}
