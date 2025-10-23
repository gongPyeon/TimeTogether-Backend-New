package timetogeter.context.place.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.place.application.dto.request.PlaceRegisterDTO;
import timetogeter.context.place.application.dto.request.UserAIInfoReqDTO;
import timetogeter.context.place.application.dto.request.UserBoardReqDTO;
import timetogeter.context.place.application.dto.response.PlaceBoardDTO;
import timetogeter.context.place.application.dto.response.PlaceRegisterResDTO;
import timetogeter.context.place.application.service.MyPlaceService;
import timetogeter.context.place.application.service.PlaceBoardService;
import timetogeter.context.place.application.service.TrainingScheduler;
import timetogeter.context.place.application.service.UserBoardService;
import timetogeter.context.promise.application.dto.response.PromiseRegisterDTO;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final PlaceBoardService placeBoardService;
    private final UserBoardService userBoardService;
    private final MyPlaceService myPlaceService;
    private final TrainingScheduler trainingScheduler;

    // 올려진 장소 확인
    @GetMapping("/{promiseId}/{page}")
    public BaseResponse<Object> viewPlaceBoard(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable("promiseId") String promiseId,
                                               @PathVariable("page") int page) {
        String userId = userPrincipal.getId();
        PlaceBoardDTO dto = placeBoardService.getPlaceBoard(userId, promiseId, page);
        return new BaseResponse<>(dto);
    }

    // 내가 올린 장소 삭제
    @DeleteMapping("/{placeId}")
    public BaseResponse<Object> deletePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable("placeId") int placeId) {
        String userId = userPrincipal.getId();
        myPlaceService.deletePlace(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE);
    }

    // 투표
    // TODO: Vote Controller의 위치
    @PostMapping("/vote/{promiseId}/{placeId}")
    public BaseResponse<Object> votePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable("promiseId") String promiseId,
                                          @PathVariable("placeId") int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.vote(userId, promiseId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_VOTE);
    }

    // 투표 취소
    @DeleteMapping("vote/{placeId}")
    public BaseResponse<Object> cancelVotePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable("placeId") int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.deleteVote(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE_VOTE);
    }

    // 장소 등록 (일반 / AI)
    // 한번에 최대 10개 등록 가능 - 확인 (중복허용, 10개)
    @PostMapping("/register/{promiseId}")
    public BaseResponse<Object> registerPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody List<PlaceRegisterDTO> dto,
                                               @PathVariable("promiseId") String promiseId) {
        String userId = userPrincipal.getId();
        myPlaceService.registerPlace(userId, promiseId, dto);
        return new BaseResponse<>(BaseCode.SUCCESS_REGISTER_PLACE);
    }

    // 장소 추천 (AI)
    @PostMapping("/ai/recommend/{promiseId}")
    public BaseResponse<Object> recommendPlace(@RequestBody UserAIInfoReqDTO reqDTO,
                                             @PathVariable("promiseId") String promiseId) {
        List<PlaceRegisterResDTO> dto = myPlaceService.recommendPlace(promiseId, reqDTO);
        return new BaseResponse<>(dto, BaseCode.SUCCESS_RECOMMEND_PLACE);
    }


    // 방장일 시 장소 확정
    // TODO: PromiseRegisterDTO의 위치
    @PostMapping("/confirm/{promiseId}/{placeId}")
    public BaseResponse<Object> confirmedPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable("promiseId") String promiseId,
                                              @PathVariable("placeId") int placeId) {
        String userId = userPrincipal.getId();
        PromiseRegisterDTO dto = placeBoardService.confirmedPlace(userId, promiseId, placeId);
        return new BaseResponse<>(dto, BaseCode.SUCCESS_CONFIRM_PLACE);
    }

    @PostMapping("rating/{placeId}")
    public BaseResponse<Object> updatePlaceRating(@RequestBody UserBoardReqDTO reqDTO,
                                                  @PathVariable("placeId") int placeId) {
        userBoardService.updatePlaceRating(placeId, reqDTO);
        return new BaseResponse<>(BaseCode.SUCCESS_PLACE_RATING);
    }

    // 장소 학습 API 구현 - 테스트용 추후에 스케쥴러만 남길 예정
    @PostMapping("/ai/train")
    public BaseResponse<Object> trainPlace() {
        trainingScheduler.sendTrainingData();
        return new BaseResponse<>(BaseCode.SUCCESS_TRAIN_PLACE);
    }

}
