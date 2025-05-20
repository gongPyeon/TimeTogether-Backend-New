package timetogeter.context.place.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import timetogeter.context.place.application.dto.PlaceDTO;
import timetogeter.context.place.application.dto.response.PlaceBoardDTO;
import timetogeter.context.place.domain.entity.Place;
import timetogeter.context.place.domain.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static timetogeter.global.common.util.PageUtil.PLACE_PAGE;

@Service
@RequiredArgsConstructor
public class PlaceBoardService { // TODO: 장소 관리 시스템

    private final PlaceRepository placeRepository;
    public PlaceBoardDTO getPlaceBoard(String userId, int page) {
        PageRequest pageRequest = PageRequest.of(page, PLACE_PAGE);
        Page<Place> placePage = placeRepository.findAll(pageRequest);

        List<PlaceDTO> places = placePage.getContent()
                .stream()
                .map(p -> new PlaceDTO(p.getPlaceId(), p.getPlaceName(), p.getPlaceUrl(), p.getVoting(), p.hasId(userId)))
                .collect(Collectors.toList());

        return new PlaceBoardDTO(page, placePage.getTotalPages(), places);
    }
}
