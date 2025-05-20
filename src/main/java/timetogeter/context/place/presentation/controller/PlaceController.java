package timetogeter.context.place.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.place.application.dto.response.PlaceBoardDTO;
import timetogeter.context.place.application.service.MyPlaceService;
import timetogeter.context.place.application.service.PlaceBoardService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final PlaceBoardService placeBoardService;
    private final MyPlaceService myPlaceService;

    @GetMapping("/{promiseId}/{page}")
    public BaseResponse<Object> viewPlaceBoard(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable int promiseId,
                                               @PathVariable int page) {
        String userId = userPrincipal.getId();
        PlaceBoardDTO dto = placeBoardService.getPlaceBoard(userId, promiseId, page);
        return new BaseResponse<>(dto);
    }

    @DeleteMapping("/{placeId}")
    public BaseResponse<Object> deletePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        myPlaceService.deletePlace(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE);
    }

    @PostMapping("vote/{placeId}")
    public BaseResponse<Object> votePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.vote(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_VOTE);
    }

    @DeleteMapping("vote/{placeId}")
    public BaseResponse<Object> cancelVotePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.deleteVote(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE_VOTE);
    }

    // TODO: 직접 입력
    @PostMapping("/")
    public BaseResponse<Object> registerPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable int promiseId) {
        String userId = userPrincipal.getId();
        myPlaceService.registerPlace(userId, promiseId);
        return new BaseResponse<>(BaseCode.SUCCESS_REGISTER_PLACE);
    }

    // TODO: AI 추천

    // TODO: AI 추천 시 선택

    // TODO: 방장일 시 장소 확정

}
