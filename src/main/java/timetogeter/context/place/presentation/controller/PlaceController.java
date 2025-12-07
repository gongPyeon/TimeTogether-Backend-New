package timetogeter.context.place.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.place.application.dto.ConfirmedPlaceDTO;
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
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@Tag(name = "장소", description = "장소관련 API")
@SecurityRequirement(name = "BearerAuth")
public class PlaceController {

    private final PlaceBoardService placeBoardService;
    private final UserBoardService userBoardService;
    private final MyPlaceService myPlaceService;
    private final TrainingScheduler trainingScheduler;

    // 올려진 장소 확인

    @Operation(summary = "장소보드", description = "공유된 장소를 확인한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),

            @ApiResponse(responseCode = "400", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "페이지 초과", summary = "페이지가 1이상이 아니거나, 총 페이지 수를 초과",
                                            value = """
                                                    { "code": 400, "message": "총 페이지를 초과한 요청이에요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{promiseId}/{page}")
    public BaseResponse<Object> viewPlaceBoard(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable("promiseId") String promiseId,
                                               @PathVariable("page") int page) {
        String userId = userPrincipal.getId();
        PlaceBoardDTO dto = placeBoardService.getPlaceBoard(userId, promiseId, page);
        return new BaseResponse<>(dto);
    }

    // 내가 올린 장소 삭제
    @Operation(summary = "공유된 장소 삭제", description = "내가 공유한 장소를 삭제한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),

            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "장소 없음", summary = "장소 아이디에 해당하는 장소가 없음",
                                            value = """
                                                    { "code": 404, "message": "장소를 찾을 수 없어요" }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403", description = "권한 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "접근 권한", summary = "사용자가 올린 장소가 아닐때",
                                            value = """
                                                    { "code": 403, "message": "접근 권한이 없어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("/{placeId}")
    public BaseResponse<Object> deletePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable("placeId") int placeId) {
        String userId = userPrincipal.getId();
        myPlaceService.deletePlace(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE);
    }

    // 투표
    // TODO: Vote Controller의 위치
    @Operation(summary = "공유된 장소 투표", description = "공유된 장소를 투표한다(무조건 1번 가능)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),

            @ApiResponse(responseCode = "400", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "논리 오류", summary = "약속원이 모두 투표함",
                                            value = """
                                                    { "code": 400, "message": "더이상 투표할 수 없어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/vote/{promiseId}/{placeId}")
    public BaseResponse<Object> votePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable("promiseId") String promiseId,
                                          @PathVariable("placeId") int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.vote(userId, promiseId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_VOTE);
    }

    // 투표 취소
    @Operation(summary = "공유된 장소 투표 취소", description = "공유된 장소에 대한 투표를 취소한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "장소 없음", summary = "장소 아이디에 해당하는 장소가 없음",
                                            value = """
                                                    { "code": 404, "message": "장소를 찾을 수 없어요" }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403", description = "권한 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "논리 오류", summary = "투표하지 않았는데 투표취소함",
                                            value = """
                                                    { "code": 403, "message": "투표 취소권한이 없어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("vote/{placeId}")
    public BaseResponse<Object> cancelVotePlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable("placeId") int placeId) {
        String userId = userPrincipal.getId();
        placeBoardService.deleteVote(userId, placeId);
        return new BaseResponse<>(BaseCode.SUCCESS_DELETE_VOTE);
    }

    // 장소 등록 (일반 / AI)
    @Operation(summary = "장소 등록", description = "장소를 등록한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "장소 등록 개수", summary = "장소 등록 개수 초과",
                                            value = """
                                                    { "code": 400, "message": "장소 등록은 최대 5개까지 가능해요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "장소등록 사 필수정보 누락", summary = "필수정보인 장소이름과 주소를 누락함",
                                            value = """
                                                    { "code": 400, "message": "장소등록에 필수정보가 누락됐어요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "장소 이름 형식", summary = "장소 이름 글자수 제한",
                                            value = """
                                                    { "code": 400, "message": "장소등록은 1자 이상 30자 이내여야해요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "장소 정보 형식", summary = "장소 정보 글자수 제한",
                                            value = """
                                                    { "code": 400, "message": "장소정보는 200자 이내여야해요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/register/{promiseId}")
    public BaseResponse<Object> registerPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody List<PlaceRegisterDTO> dto,
                                               @PathVariable("promiseId") String promiseId) {
        String userId = userPrincipal.getId();
        myPlaceService.registerPlace(userId, promiseId, dto);
        return new BaseResponse<>(BaseCode.SUCCESS_REGISTER_PLACE);
    }

    // 장소 추천 (AI)
    @Operation(summary = "AI로 장소 추천", description = "사용자 맞춤형 장소를 추천한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "약속 없음", summary = "약속 아이디에 해당하는 약속이 없음",
                                            value = """
                                                    { "code": 404, "message": "약속을 찾을 수 없어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/ai/recommend")
    public BaseResponse<Object> recommendPlace(@RequestBody UserAIInfoReqDTO reqDTO) {
        List<PlaceRegisterResDTO> dto = myPlaceService.recommendPlace(reqDTO);
        return new BaseResponse<>(dto, BaseCode.SUCCESS_RECOMMEND_PLACE);
    }


    // 방장일 시 장소 확정
    // TODO: PromiseRegisterDTO의 위치
    @Operation(summary = "장소 확정", description = "방장일 시 장소를 확정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "약속 없음", summary = "약속 아이디에 해당하는 약속이 없음",
                                            value = """
                                                    { "code": 404, "message": "약속을 찾을 수 없어요" }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403", description = "권한 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "권한 오류", summary = "약속장이 아닌데 장소를 확정함",
                                            value = """
                                                    { "code": 403, "message": "약속장에 대한 권한이 없어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/confirm/{promiseId}/{placeId}")
    public BaseResponse<Object> confirmedPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable("promiseId") String promiseId,
                                               @PathVariable("placeId") int placeId,
                                               @RequestParam(value = "aiPlaceId", required = false) Integer aiPlaceId) {
        String userId = userPrincipal.getId();
        int aiPlaceValue = (aiPlaceId != null) ? aiPlaceId : -1;
        PromiseRegisterDTO dto = placeBoardService.confirmedPlace(userId, promiseId, placeId, aiPlaceValue);
        return new BaseResponse<>(dto, BaseCode.SUCCESS_CONFIRM_PLACE);
    }

    @PostMapping("rating/{placeId}")
    @Operation(summary = "장소 평점 업데이트", description = "사용자의 장소 평점을 업데이트한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "평점 형식 오류", summary = "장소 평점 범위를 벗어남",
                                            value = """
                                                    { "code": 400, "message": "장소 평점 범위는 1~5에요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    public BaseResponse<Object> updatePlaceRating(@RequestBody UserBoardReqDTO reqDTO,
                                                  @PathVariable("placeId") int placeId) {
        userBoardService.updatePlaceRating(placeId, reqDTO);
        return new BaseResponse<>(BaseCode.SUCCESS_PLACE_RATING);
    }

    // 장소 학습 API 구현 - 테스트용 추후에 스케쥴러만 남길 예정
    @Profile("dev")
    @Operation(summary = "[DEV ONLY] AI 학습 트리거", description = "개발 환경에서만 AI 학습을 수동으로 트리거한다")
    @PostMapping("/ai/train")
    public BaseResponse<Object> trainPlace() {
        trainingScheduler.sendTrainingData();
        return new BaseResponse<>(BaseCode.SUCCESS_TRAIN_PLACE);
    }

    @Operation(summary = "장소 확정 확인", description = "장소를 확정했는지 확인한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "약속 없음", summary = "약속 아이디에 해당하는 약속이 없음",
                                            value = """
                                                    { "code": 404, "message": "약속을 찾을 수 없어요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "장소 없음", summary = "약속 아이디에 해당하는 장소가 없음",
                                            value = """
                                                    { "code": 404, "message": "장소가 확정되지 않았어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @GetMapping("/confirm/{promiseId}")
    public BaseResponse<Object> confirmedPlaceCheck(@PathVariable("promiseId") String promiseId) {
        ConfirmedPlaceDTO dto = placeBoardService.confirmedPlaceCheck(promiseId);
        return new BaseResponse<>(dto, BaseCode.SUCCESS_CONFIRM_PLACE);
    }

}
