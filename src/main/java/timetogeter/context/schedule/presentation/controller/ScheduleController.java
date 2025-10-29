package timetogeter.context.schedule.presentation.controller;

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
import timetogeter.context.schedule.application.dto.request.ScheduleConfirmReqDTO;
import timetogeter.context.schedule.application.service.ConfirmedScheduleService;
import timetogeter.context.time.application.dto.response.TimeBoardResDTO;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Tag(name = "약속 확정", description = "약속 관련 API")
@SecurityRequirement(name = "BearerAuth")
public class ScheduleController {

    private final ConfirmedScheduleService confirmedScheduleService;

    @Operation(summary = "약속 일정 확정", description = "약속 일정을 확정한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "약속 공유키 없음", summary = "약속 공유키에 해당하는 약속이 없음",
                                            value = """
                                                    { "code": 404, "message": "약속 공유키 테이블을 찾을 수 없어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/confirm/{groupId}")
    public BaseResponse<Object> confirmSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable("groupId") String groupId,
                                                @RequestBody ScheduleConfirmReqDTO reqDTO) {
        String userId = userPrincipal.getId();
        confirmedScheduleService.confirmSchedule(userId, groupId, reqDTO);
        return new BaseResponse<>(BaseCode.SUCCESS_SCHEDULE_UPDATE);
    }
}
