package timetogeter.context.place.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.place.application.dto.request.PlaceRegisterDTO;
import timetogeter.context.place.application.dto.response.PlaceBoardDTO;
import timetogeter.context.place.application.service.MyPlaceService;
import timetogeter.context.place.application.service.PlaceBoardService;
import timetogeter.context.promise.application.dto.response.PromiseRegisterDTO;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final PlaceBoardService placeBoardService;
    private final MyPlaceService myPlaceService;

    // 올려진 장소 확인
    @GetMapping("/{promiseId}/{page}")
    public BaseResponse<Object> viewPlaceBoard(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable int promiseId,
                                               @PathVariable int page) {
        String userId = userPrincipal.getId();
        PlaceBoardDTO dto = placeBoardService.getPlaceBoard(userId, promiseId, page);
        return new BaseResponse<>(dto);
    }

    // 내가 올린 장소 삭제
    @DeleteMapping("/{placeId}")
    public BaseResponse<Object> deletePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        myPlaceService.deletePlace(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE);
    }

    // 투표
    // TODO: Vote Controller의 위치
    @PostMapping("vote/{placeId}")
    public BaseResponse<Object> votePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.vote(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_VOTE);
    }

    // 투표 취소
    @DeleteMapping("vote/{placeId}")
    public BaseResponse<Object> cancelVotePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.deleteVote(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE_VOTE);
    }

    // 장소 등록 (일반 / AI)
    // TODO: 한번에 최대 10개 등록 가능 - 확인 (중복, 10개)
    @PostMapping("/register/{promiseId}")
    public BaseResponse<Object> registerPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody List<PlaceRegisterDTO> dto,
                                               @PathVariable int promiseId) {
        String userId = userPrincipal.getId();
        myPlaceService.registerPlace(userId, promiseId, dto);
        return new BaseResponse<>(BaseCode.SUCCESS_REGISTER_PLACE);
    }

    // TODO: AI 추천 최대 10개 반환 - 확인 (FAST API JSON 형식이 나오면 적용 예정)
    @PostMapping("/check/ai/{promiseId}")
    public BaseResponse<Object> checkAIPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable int promiseId) {
        String userId = userPrincipal.getId();
        myPlaceService.recommendPlace(userId, promiseId);
        return new BaseResponse<>(BaseCode.SUCCESS_REGISTER_PLACE);
    }


    // 방장일 시 장소 확정
    // TODO: PromiseRegisterDTO의 위치
    @PostMapping("/confirm/{placeId}")
    public BaseResponse<Object> confirmedPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable String promiseId,
                                              @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        PromiseRegisterDTO dto = placeBoardService.confirmedPlace(userId, promiseId, placeId);
        return new BaseResponse<>(dto);
    }

    // 방장일 시 장소 수정
    @PostMapping("/confirm/re/{placeId}")
    public BaseResponse<Object> reConfirmedPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable String promiseId,
                                                 @PathVariable int placeId) {
        String userId = userPrincipal.getId();
        PromiseRegisterDTO dto = placeBoardService.reConfirmedPlace(userId, promiseId, placeId);
        return new BaseResponse<>(dto);
    }

}
