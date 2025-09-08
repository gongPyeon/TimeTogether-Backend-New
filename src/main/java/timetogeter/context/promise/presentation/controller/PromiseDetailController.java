package timetogeter.context.promise.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.promise.application.dto.request.detail.PromiseView3Request;
import timetogeter.context.promise.application.dto.request.detail.PromiseView4Request;
import timetogeter.context.promise.application.dto.request.detail.Promiseview2Request;
import timetogeter.context.promise.application.dto.response.detail.PromiseView1Response;
import timetogeter.context.promise.application.dto.response.detail.PromiseView2Response;
import timetogeter.context.promise.application.dto.response.detail.PromiseView3Response;
import timetogeter.context.promise.application.dto.response.detail.PromiseView4Response;
import timetogeter.context.promise.application.service.PromiseDetailInfoService;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/promise")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "약속", description = "약속 생성, 약속 확인 , 약속 멤버 초대/참여하기 API")
public class PromiseDetailController {
    private final PromiseDetailInfoService promiseDetailInfoService;

//======================
// 사용자가 속한 그룹 내 약속을 보여주는 화면 (Step1,2,3,4)
//======================

    /*
    디테일 확인 - 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 - Step1

    [웹] 로그인 상태로 토큰 담아 요청 ->
    [서버] PromiseProxyUser에서 encPromiseId(개인키로 암호화한 약속 아이디) 반환
     */
    @Operation(summary = "약속 디테일 - Step1", description = """
            로그인한 사용자가 속한 그룹 내 약속을 조회합니다.

            - 요청: 사용자 인증 (UserPrincipal)
            - 처리: PromiseProxyUser에서 encPromiseId(개인키로 암호화된 약속 아이디) 조회
            - 반환: 개인키로 암호화된 약속 아이디 리스트
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약속 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping(value = "/view1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<PromiseView1Response>> view1(
            @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception{
        String userId = userPrincipal.getId();
        List<PromiseView1Response> response = promiseDetailInfoService.getEncPromiseIdList(userId);
        return new BaseResponse<>(response);
    }

    /*
    디테일 확인 - 사용자가 속한 그룹 내 약속을 (정하는중) 로 구분지어 보여주는 화면 - Step2

   [웹] promiseId를 담아 요청 ->
   [서버] Promise, PromiseCheck 테이블에서 약속 확정 여부(정하는중) 약속 정보 반환
    */
    @Operation(summary = "약속 디테일 - Step2", description = """
            사용자가 속한 그룹 내 (정하는중) 약속 정보를 조회합니다.

            - 요청: 사용자 인증 (UserPrincipal) + Promiseview2Request (encPromiseId 리스트)
            - 처리: Promise, PromiseCheck 테이블에서 약속 확정 여부 조회
            - 반환: 약속 정보 리스트
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약속 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/view2", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<PromiseView2Response>> view2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Promiseview2Request request) throws Exception{
        String userId = userPrincipal.getId();
        List<PromiseView2Response> response = promiseDetailInfoService.getPromiseInfoList(userId, request);
        return new BaseResponse<>(response);
    }


    /*
   디테일 확인 - 사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 - Step3

   [웹] 개인키로 암호화한 약속키를 담아 요청 ->
   [서버] PromiseShareKey에서 encPromiseKey로 해당되는 scheduleId들을 반환
   */
    @Operation(summary = "약속 디테일 - Step3", description = """
            사용자가 속한 그룹 내 (정하는중 + 확정완료) 약속별 scheduleId 조회

            - 요청: 사용자 인증 (UserPrincipal) + PromiseView3Request (encPromiseKey 리스트)
            - 처리: PromiseShareKey에서 encPromiseKey로 scheduleId 조회
            - 반환: scheduleId 리스트
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "scheduleId 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/view3", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<PromiseView3Response>> view3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PromiseView3Request request) throws Exception{
        String userId = userPrincipal.getId();
        List<PromiseView3Response> response = promiseDetailInfoService.getScheduleIdList(userId, request);
        return new BaseResponse<>(response);
    }

    /*
    디테일 확인 - 사용자가 속한 그룹 내 약속을 (확정완료) 로 구분지어 보여주는 화면 - Step4

    [웹] 개인키로 암호화한 약속키를 담아 요청 ->
    [서버] PromiseShareKey에서 encPromiseKey로 해당되는 scheduleId들을 반환
    */
    @Operation(summary = "약속 디테일 - Step4", description = """
            사용자가 속한 그룹 내 확정 완료된 약속별 scheduleId 조회

            - 요청: 사용자 인증 (UserPrincipal) + PromiseView4Request (encPromiseKey 리스트)
            - 처리: PromiseShareKey에서 encPromiseKey로 scheduleId 조회
            - 반환: scheduleId 리스트
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "scheduleId 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/view4", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<PromiseView4Response>> view4(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PromiseView4Request request) throws Exception{
        String userId = userPrincipal.getId();
        List<PromiseView4Response> response = promiseDetailInfoService.getScheduleInfoList(userId, request);
        return new BaseResponse<>(response);
    }
}
