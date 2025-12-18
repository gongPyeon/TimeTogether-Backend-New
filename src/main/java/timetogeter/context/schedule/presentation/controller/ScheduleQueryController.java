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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.request.PromiseSearchReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseDetailResDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.application.service.ConfirmedScheduleService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promise")
@Tag(name = "약속일정 및 히스토리 조회", description = "약속일정과 히스토리 관련 API")
@SecurityRequirement(name = "BearerAuth")
public class ScheduleQueryController {

    private final ConfirmedScheduleService confirmedScheduleService;

    @Operation(summary = "약속일정 조회", description = "약속일정을 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/get")
    public BaseResponse<Object> getPromiseView(@RequestBody GetPromiseBatchReqDTO reqDTO){
        String userId = reqDTO.pseudoId();
        PromiseListResDTO dto = confirmedScheduleService.getPromiseView(reqDTO, userId);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "그룹별 약속일정 조회", description = "그룹별 약속일정을 조회한다")
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
    @PostMapping("/get/{groupId}")
    public BaseResponse<Object> getPromiseView(
            @PathVariable("groupId") String groupId,
            @RequestBody GetPromiseBatchReqDTO reqDTO){
        String userId = reqDTO.pseudoId();
        PromiseListResDTO dto = confirmedScheduleService.getPromiseViewByGroup(groupId, reqDTO, userId);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "약속일정 상세 조회", description = "약속일정을 상세 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "일정이 없음", summary = "일정 아이디에 해당하는 일정이 없음",
                                            value = """
                                                    { "code": 404, "message": "일정이 존재하지 않아요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @GetMapping("/get/{scheduleId}/detail")
    public BaseResponse<Object> getPromiseDetailView(
            @PathVariable("scheduleId") String scheduleId){
        PromiseDetailResDTO dto = confirmedScheduleService.getPromiseDetailView(scheduleId);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "약속일정 검색", description = "약속일정을 검색한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/search")
    public BaseResponse<Object> searchPromiseView(
            @RequestParam("query") String query,
            @RequestBody PromiseSearchReqDTO reqDTO){
        String userId = reqDTO.pseudoId();
        PromiseListResDTO dto = confirmedScheduleService.searchPromiseView(query, userId);
        return new BaseResponse<>(dto);
    }


}
