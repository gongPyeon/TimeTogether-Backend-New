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
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.schedule.application.dto.request.CalendarCreateRequest1;
import timetogeter.context.schedule.application.dto.request.CalendarCreateRequest2;
import timetogeter.context.schedule.application.dto.request.CalendarRewriteRequest1;
import timetogeter.context.schedule.application.dto.request.CalendarViewRequest1;
import timetogeter.context.schedule.application.dto.request.CalendarViewRequest2;
import timetogeter.context.schedule.application.dto.response.CalendarCreateResponse1;
import timetogeter.context.schedule.application.dto.response.CalendarCreateResponse2;
import timetogeter.context.schedule.application.dto.response.CalendarRewriteResponse1;
import timetogeter.context.schedule.application.dto.response.CalendarViewResponse1;
import timetogeter.context.schedule.application.dto.response.CalendarViewResponse2;
import timetogeter.context.schedule.application.service.CalendarDetailService;
import timetogeter.context.schedule.application.service.CalendarViewService;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "캘린더 관리", description = "캘린더 일정 등록 및 조회 API")
@SecurityRequirement(name = "BearerAuth")
public class CalendarManageController {

    private final CalendarViewService calendarViewService;
    private final CalendarDetailService calendarDetailService;


//======================
// 캘린더 - 캘린더 메인, 캘린더 일정 확인 (Step1,2)
//======================

    /*
    캘린더 - 캘린더 메인, 일정 확인 - step1

    [웹] timeStampInfo 리스트를 요청
        /api/v1/calendar/view1

    [서버] timeStampInfo와 userId로 encTimeStamp 리스트를 반환
     */
    @Operation(summary = "개인 일정 조회 Step1", description = "timeStampInfo 리스트로 encTimeStamp 리스트를 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "성공 응답", summary = "encTimeStamp 리스트 반환",
                                            value = """
                                                    {
                                                        "code": 200,
                                                        "message": "요청에 성공했습니다.",
                                                        "result": {
                                                            "encTimeStampList": ["encTimeStamp1", "encTimeStamp2"]
                                                        }
                                                    }
                                                    """
                                    )
                            }
                    ))
    })
    @PostMapping(value = "/view1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CalendarViewResponse1> viewCalendar1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CalendarViewRequest1 requests) throws Exception{
        String userId = userPrincipal.getId();
        CalendarViewResponse1 response = calendarViewService.viewCalendar1(requests, userId);
        return new BaseResponse<>(response);
    }

    /*
    캘린더 - 캘린더 메인, 일정 확인 - step2

    [웹] scheduleId를 리스트 형태로 요청
        /api/v1/calendar/view2

    [서버] scheduleId에 해당하는 Schedule 정보와 장소 정보를 리스트 형태로 반환
     */
    @Operation(summary = "개인 일정 조회 Step2", description = "scheduleId 리스트로 스케줄 정보와 장소 정보를 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "성공 응답", summary = "스케줄 및 장소 정보 리스트 반환",
                                            value = """
                                                    {
                                                        "code": 200,
                                                        "message": "요청에 성공했습니다.",
                                                        "result": [
                                                            {
                                                                "scheduleId": "scheduleId1",
                                                                "title": "러닝500m",
                                                                "content": "힙으뜸 유투버와 달리기",
                                                                "purpose": "러닝 습관화",
                                                                "placeName": "백두산 상류 50000M지점",
                                                                "placeAddr": "북한 백두산 백록담",
                                                                "placeInfo": "아무거나 그냥 설명"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    )
                            }
                    ))
    })
    @PostMapping(value = "/view2", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<CalendarViewResponse2>> viewCalendar2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CalendarViewRequest2 requests) throws Exception{
        List<CalendarViewResponse2> response = calendarViewService.viewCalendar2(requests);

        return new BaseResponse<>(response);
    }



//======================
// 캘린더 - 일정등록 (Step1)
//======================

    /*
    [웹] Schedule 내 저장할
        title, content, type, place, placeUrl, startDateTime, endDateTime,
        encPromiseKey(개인키를 개인키로 암호화), encUserId(유저아이디를 개인키로 암호화)
        내용 담아 요청
    /api/v1/calendar/create1

    [서버] Schedule 테이블, PromiseShareKey 테이블 내에 저장
     */
    @Operation(summary = "일정 등록 Step1", description = "일정 기본 정보(제목, 내용, 목적, 장소)를 등록한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "성공 응답", summary = "일정 등록 성공",
                                            value = """
                                                    {
                                                        "code": 200,
                                                        "message": "요청에 성공했습니다.",
                                                        "result": {
                                                            "scheduleId": "3AFygat7JT7bK9jsksrnM7V0Hl9XMkJ7iB9mwzOrW+G+YVB6mKyHuJPnWZQo0SJDaQ==",
                                                            "title": "러닝500m",
                                                            "content": "힙으뜸 유투버와 달리기"
                                                        }
                                                    }
                                                    """
                                    )
                            }
                    ))
    })
    @PostMapping(value = "/create1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CalendarCreateResponse1> createCalendar1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CalendarCreateRequest1 requests) throws Exception{
        String userId = userPrincipal.getId();
        CalendarCreateResponse1 response = calendarDetailService.createCalendar1(requests, userId);
        return new BaseResponse<>(response);
    }

    /*
    [웹] 개인 일정 저장
        encStartTimeAndEndTime, timeStampInfo를 요청
    /api/v1/calendar/create2

    [서버] TimeStamp 테이블에 저장하고 성공 메시지 반환
     */
    @Operation(summary = "일정 등록 Step2", description = "개인 일정의 시간 정보(encStartTimeAndEndTime, timeStampInfo)를 저장한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "성공 응답", summary = "개인 일정 저장 성공",
                                            value = """
                                                    {
                                                        "code": 200,
                                                        "message": "요청에 성공했습니다.",
                                                        "result": {
                                                            "msg": "개인 일정을 저장했습니다."
                                                        }
                                                    }
                                                    """
                                    )
                            }
                    ))
    })
    @PostMapping(value = "/create2", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CalendarCreateResponse2> createCalendar2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CalendarCreateRequest2 requests) throws Exception{
        String userId = userPrincipal.getId();
        CalendarCreateResponse2 response = calendarDetailService.createCalendar2(requests, userId);
        return new BaseResponse<>(response);
    }

//======================
// 캘린더 - 일정 수정 (Step1)
//======================

    /*
    [웹] encPromiseKey와 수정하는 부분 함께 요청
        /api/v1/calendar/rewrite1

    [서버] scheduleId에 해당하는 것에 수정해서 반환
     */
    @PostMapping(value = "/rewrite1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CalendarRewriteResponse1> rewriteCalendar1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CalendarRewriteRequest1 requests) throws Exception{
        String userId = userPrincipal.getId();
        CalendarRewriteResponse1 response = calendarDetailService.rewriteCalendar1(requests);
        return new BaseResponse<>(response);
    }


}
