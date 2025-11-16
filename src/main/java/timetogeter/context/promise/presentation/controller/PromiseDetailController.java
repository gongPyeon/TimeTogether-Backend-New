package timetogeter.context.promise.presentation.controller;

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
    @Operation(
            summary = "약속 디테일 - Step1",
            description = """
                로그인한 사용자가 속한 약속 아이디를 모두 조회합니다.

                - 요청: accesstoken -> 사용자 인증 (UserPrincipal)
                - 처리: PromiseProxyUser에서 encPromiseId(개인키로 암호화된 약속 아이디) 조회
                - 반환: 개인키로 암호화된 약속 아이디 리스트
                - 이후 작업: 개인키로 복호화해 약속 아이디 리스트 얻음
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "약속 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PromiseView1Response.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "code": 200,
                                  "message": "요청에 성공했습니다.",
                                  "result": [
                                    {
                                      "encPromiseId": "0cL0PMOtGq5i6nZnJKlrdXQhdZNxJa8Kumur5fUQrxvYps58yM2OIXDP+/D6IHwa8/w0cw=="
                                    }
                                  ]
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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
    디테일 확인 - 사용자가 속한 그룹 내 약속 (정하는중) 구분지어 보여주는 화면 - Step2

   [웹] promiseId를 담아 요청 ->
   [서버] Promise, PromiseCheck 테이블에서 약속 확정 여부(정하는중) 약속 정보 반환
    */
    @Operation(
            summary = "약속 디테일 - Step2",
            description = """
                사용자가 속한 그룹 내 정하는 중인 약속 정보를 조회합니다.

                - 요청: 사용자 인증 (UserPrincipal) + groupId, promiseIdList
                - 처리: Promise, PromiseCheck 테이블에서 약속 확정 여부 조회
                - 반환: 정하는 중인 약속이 존재하면, 해당 약속 정보 반환
                - 이후 작업: 반환된 약속 정보중 promiseId를 리스트 형태로 이후에 요청해야함
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "약속 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PromiseView2Response.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "code": 200,
                                  "message": "요청에 성공했습니다.",
                                  "result": [
                                    {
                                      "isConfirmed": false,
                                      "promiseId": "e3f971e9-0e41-48b2-bb2e-b7594b98e170",
                                      "title": "초콜릿모임",
                                      "type": "스터디",
                                      "startDate": "2025-11-14",
                                      "endDate": "2025-11-19",
                                      "managerId": "hyerihyeri",
                                      "promiseImg": "빼빼로만들기-초콜릿"
                                    }
                                  ]
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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
    @Operation(
            summary = "약속 디테일 - Step3",
            description = """
                사용자가 속한 그룹 내 약속별 scheduleId 조회

                - 요청: 사용자 인증 (UserPrincipal) + promiseIdList
                - 처리: PromiseShareKey에서 encPromiseKey로 scheduleId 조회
                - 반환: scheduleId 리스트 
                - 이후 작업: 다음 요청에서 scheduleId 리스트 필요함
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "scheduleId 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PromiseView3Response.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "code": 200,
                                  "message": "요청에 성공했습니다.",
                                  "result": [
                                    {
                                      "scheduleId": "61a4c8e6-ea48-47d3-9523-9cf09dd6aae4"
                                    }
                                  ]
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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
    @Operation(
            summary = "약속 디테일 - Step4",
            description = """
                사용자가 속한 그룹 내 확정 완료된 약속(=스케줄)의 scheduleId 조회

                - 요청: 사용자 인증 (UserPrincipal) + ScheduleIdListRequest
                - 처리: 스케줄 ID로 확정된 약속 정보 조회
                - 반환: 확정된 약속 정보 리스트
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "scheduleId 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PromiseView4Response.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "code": 200,
                                  "message": "요청에 성공했습니다.",
                                  "result": [
                                    {
                                      "isConfirmed": true,
                                      "scheduleId": "61a4c8e6-ea48-47d3-9523-9cf09dd6aae4",
                                      "title": "61a4c8e6-ea48-47d3-9523-9cf09dd6aae4",
                                      "content": "초콜릿다음엔?",
                                      "purpose": "초콜릿초콜릿",
                                      "placeId": 1,
                                      "groupId": "d71ac3eb-fc61-4cff-92c7-478a0e092936"
                                    }
                                  ]
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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
