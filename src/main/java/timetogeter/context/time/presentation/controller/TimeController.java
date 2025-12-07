package timetogeter.context.time.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.place.application.dto.ConfirmedPlaceDTO;
import timetogeter.context.promise.application.dto.response.PromiseRegisterDTO;
import timetogeter.context.time.application.dto.ConfirmedTimeDTO;
import timetogeter.context.time.application.dto.request.ConfirmDateReqDTO;
import timetogeter.context.time.application.dto.request.TimeSlotReqDTO;
import timetogeter.context.time.application.dto.request.UserTimeSlotReqDTO;
import timetogeter.context.time.application.dto.response.TimeBoardResDTO;
import timetogeter.context.time.application.dto.response.UserScheduleResDTO;
import timetogeter.context.time.application.dto.response.UserTimeBoardResDTO;
import timetogeter.context.time.application.service.MyTimeService;
import timetogeter.context.time.application.service.TimeBoardService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/time")
@Tag(name = "시간", description = "시간 관련 API")
@SecurityRequirement(name = "BearerAuth")
public class TimeController {

    private final TimeBoardService timeBoardService;
    private final MyTimeService myTimeService;

    @Operation(summary = "시간보드", description = "약속의 시간을 확인한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),

            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "약속 시간범위 정보 누락", summary = "시간범위가 벗어났거나 유효하지 않음",
                                            value = """
                                                    { "code": 404, "message": "약속에 시간범위 정보가 존재하지 않아요" }
                                                    """
                                    ),
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
    @GetMapping("/{promiseId}") // TODO: 엔티티 네이밍
    public BaseResponse<Object> viewTimeBoard(@PathVariable("promiseId") String promiseId) {
        TimeBoardResDTO dto = timeBoardService.getTimeBoard(promiseId);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "가능한 시간 조회", description = "약속 시간을 선택했을때 가능한 / 불가능한 사람을 확인한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/{promiseId}")
    public BaseResponse<Object> viewUsersByTime(@PathVariable("promiseId") String promiseId,
                                                @RequestBody TimeSlotReqDTO reqDTO) {
        UserTimeBoardResDTO dto = timeBoardService.getUsersByTime(promiseId, reqDTO);
        return new BaseResponse<>(dto);
    }


    @Operation(summary = "내 시간표 업데이트", description = "약속에 내 시간을 업데이트한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/my/{promiseId}")
    public BaseResponse<Object> updateUserTime(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable("promiseId") String promiseId,
                                               @RequestBody UserTimeSlotReqDTO reqDTO) {
        String userId = userPrincipal.getId();
        myTimeService.updateUserTime(userId, promiseId, reqDTO);
        return new BaseResponse<>(BaseCode.TIME_OK);
    }

    @Operation(summary = "스케줄 로드", description = "개인 시간표로부터 스케줄을 로드한다(스케쥴 아이디 반환)")
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
    @GetMapping("/my/schedule/{promiseId}")
    public BaseResponse<Object> loadUserSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable("promiseId") String promiseId) {
        String userId = userPrincipal.getId();
        UserScheduleResDTO dto = myTimeService.loadUserSchedule(userId, promiseId);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "약속 시간 확정", description = "약속 시간을 확정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),

            @ApiResponse(responseCode = "403", description = "권한 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "약속장 권한", summary = "약속장이 아닌데 확정할 경우",
                                            value = """
                                                    { "code": 403, "message": "약속장에 대한 접근 권한이 없어요" }
                                                    """
                                    )
                            }
                    )
            ),
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
    @PostMapping("/confirm/{promiseId}")
    public BaseResponse<Object> confirmDateTime(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable("promiseId") String promiseId,
                                                @RequestBody ConfirmDateReqDTO reqDTO) {
        String userId = userPrincipal.getId();
        PromiseRegisterDTO dto = timeBoardService.confirmDateTime(userId, promiseId, reqDTO.dateTime());
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "시간 확정 확인", description = "시간을 확정했는지 확인한다")
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
                                            name = "시간 없음", summary = "약속 아이디에 해당하는 시간이 없음",
                                            value = """
                                                    { "code": 404, "message": "시간이 확정되지 않았어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/confirm/{promiseId}")
    public BaseResponse<Object> confirmedTimeCheck(@PathVariable("promiseId") String promiseId) {
        ConfirmedTimeDTO dto = timeBoardService.confirmedTimeCheck(promiseId);
        return new BaseResponse<>(dto, BaseCode.SUCCESS_CONFIRM_TIME);
    }
}
