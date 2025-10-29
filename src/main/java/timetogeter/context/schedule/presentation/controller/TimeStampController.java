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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.request.TimestampReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.application.dto.response.TimestampResDTO;
import timetogeter.context.schedule.application.service.TimeStampQueryService;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timestamp")
@Tag(name = "타임스탬프", description = "타임스탬프 (스케줄 조회) 관련 API")
@SecurityRequirement(name = "BearerAuth")
public class TimeStampController {
    private final TimeStampQueryService timeStampQueryService;

    @Operation(summary = "타임스탬프 조회", description = "타임스탬프를 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/get")
    public BaseResponse<Object> getTimeStampList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @RequestBody TimestampReqDTO reqDTO){
        String userId = userPrincipal.getId();
        TimestampResDTO dto = timeStampQueryService.getTimeStampList(userId, reqDTO);
        return new BaseResponse<>(dto);
    }
}
