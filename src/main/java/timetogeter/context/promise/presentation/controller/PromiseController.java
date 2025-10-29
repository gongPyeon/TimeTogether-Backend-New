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
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.promise.application.dto.request.ExitPromiseReqDTO;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise1Request;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise2Request;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise3Request;
import timetogeter.context.promise.application.dto.request.basic.CreatePromise4Request;
import timetogeter.context.promise.application.dto.response.UserInfoResDTO;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise1Response;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise2Response;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise3Response;
import timetogeter.context.promise.application.dto.response.basic.CreatePromise4Response;
import timetogeter.context.promise.application.service.PromiseManageInfoService;
import timetogeter.context.promise.application.service.PromiseSecurityService;
import timetogeter.context.promise.application.dto.response.UserIdsResDTO;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promise")
@Tag(name = "약속", description = "약속 생성, 약속 확인, 약속 멤버 초대/참여하기/나가기 API")
public class PromiseController {

    private final PromiseSecurityService promiseSecurityService;
    private final PromiseManageInfoService promiseManageInfoService;

    @Operation(summary = "암호화된 약속원들의 아이디 조회", description = "암호화된 약속원들의 아이디를 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/mem/s1/{promiseId}")
    public BaseResponse<Object> getUsersByPromiseTime1(@PathVariable("promiseId") String promiseId) {
        UserIdsResDTO dto = promiseSecurityService.getUsersByPromiseTime(promiseId);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "약속원 정보 조회", description = "약속원에 대한 정보를 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/mem/s2/{promiseId}")
    public BaseResponse<Object> getUsersByPromiseTime2(@PathVariable("promiseId") String promiseId,
                                                       @RequestBody UserIdsResDTO reqDTO) {
        UserInfoResDTO dto = promiseSecurityService.getUserInfoByDTO(promiseId, reqDTO);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "약속 나가기", description = "약속을 나갈 경우 약속관련 테이블을 삭제한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/exit")
    public BaseResponse<Object> exitPromise(@RequestBody ExitPromiseReqDTO reqDTO) {
        promiseSecurityService.exitPromise(reqDTO);
        return new BaseResponse<>(BaseCode.SUCCESS_EXIT_PROMISE);
    }


//======================
// 약속 만들기 - 기본 정보 입력 (Step1,2,3,4)
//======================

    /*
    약속 만들기 - 기본 정보 입력 Step1

    [웹] 그룹원이 userId, encGroupId(개인키로 암호화한 groupId)를 담은 request를 요청
        /promise/create1 ->
    [서버] GroupProxyUser의 userId에 해당하는 '개인키로 암호화된 그룹 아이디', '개인키로 암호화한
		  (그룹키로 암호화한 사용자 고유 아이디)'를 리스트 형태로 반환
     */
    @Operation(summary = "약속 만들기 - Step1", description = """
        사용자 개인키로 암호화된 그룹 정보를 조회하는 단계입니다.

        - 요청: 사용자 인증 (UserPrincipal) + CreatePromise1Request
        - 처리: GroupProxyUser에서 userId 기반 그룹 정보 조회
        - 반환: 개인키로 암호화된 그룹 아이디와 그룹키로 암호화된 사용자 고유 아이디 리스트
        """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "그룹 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"code\": 401, \"message\": \"인증이 필요합니다.\" }")
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"code\": 500, \"message\": \"서버 내부 오류가 발생했습니다.\" }")
                    ))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/create1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromise1Response> createPromise1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromise1Request request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromise1Response response = promiseManageInfoService.createPromise1(userId,request);
        return new BaseResponse<>(response);
    }

    /*
    약속 만들기 - 기본 정보 입력 Step2

    [웹] 개인키로 암호화된 그룹 아이디, 암호화된 (그룹키로 암호화된 사용자 고유 아이디) 복호화 후
			그룹 아이디(저장해두기), (그룹키로 암호화된 사용자 고유 아이디)를 리스트 형태로 담아 요청
			/promise/create2 ->
    [서버] 그룹 아이디, (그룹키로 암호화한 사용자 고유 아이디)에 해당하는
			GroupShareKey테이블 내 "개인키로 암호화한 그룹키" 넘김
    */
    @Operation(summary = "약속 만들기 - Step2", description = """
        개인키로 암호화된 그룹 아이디와 사용자 고유 아이디를 복호화하여 GroupShareKey 정보를 조회합니다.

        - 요청: 사용자 인증 (UserPrincipal) + CreatePromise2Request
        - 처리: 그룹 아이디, 사용자 고유 아이디 기준으로 GroupShareKey 조회
        - 반환: 개인키로 암호화한 그룹키 리스트
        """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "GroupShareKey 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/create2", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromise2Response> createPromise2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromise2Request request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromise2Response response = promiseManageInfoService.createPromise2(userId,request);
        return new BaseResponse<>(response);
    }

    /*
    약속 만들기 - 기본 정보 입력  Step3

    [웹] 개인키로 "개인키로 암호화한 그룹키" 복호화후 저장해 두었다가,
		이전 저장한 그룹 아이디를 GroupShareKey테이블 내 레코드 리스트 요청
		 /promise/create3
    [서버] 해당 그룹 아이디에 해당하는 레코드들 리스트로 반환 (그룹 내 그룹원들 볼 수 있음)
    */
    @Operation(summary = "약속 만들기 - Step3", description = """
        개인키로 암호화한 그룹키를 복호화 후 이전 저장한 그룹 아이디 기준으로 GroupShareKey 레코드 리스트 조회.

        - 요청: CreatePromise3Request
        - 처리: 그룹 내 그룹원 정보 조회
        - 반환: 그룹원 리스트
        """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "그룹원 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/create3", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromise3Response> createPromise3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromise3Request request) throws Exception{
        CreatePromise3Response response = promiseManageInfoService.createPromise3(request);
        return new BaseResponse<>(response);
    }

    /*
    약속 만들기 - 기본 정보 입력  Step4

    [웹] 개인키로 "개인키로 암호화한 그룹키" 복호화후 저장해 두었다가,
		이전 저장한 그룹 아이디를 GroupShareKey테이블 내 레코드 리스트 요청
		 /promise/create4
    [서버] 해당 그룹 아이디에 해당하는 레코드들 리스트로 반환 (그룹 내 그룹원들 볼 수 있음)
    */
    @Operation(summary = "약속 만들기 - Step4", description = """
        개인키로 암호화한 그룹키를 복호화 후 이전 저장한 그룹 아이디 기준으로 GroupShareKey 레코드 리스트 조회 (최종 단계).

        - 요청: 사용자 인증 (UserPrincipal) + CreatePromise4Request
        - 처리: 그룹 내 그룹원 정보 조회
        - 반환: 그룹원 리스트
        """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "그룹원 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/create4", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromise4Response> createPromise4(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromise4Request request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromise4Response response = promiseManageInfoService.createPromise4(userId,request);
        return new BaseResponse<>(response);
    }

}
