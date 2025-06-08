package timetogeter.context.promise.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.promise.application.dto.request.detail.OverallPromiseViewRequest3;
import timetogeter.context.promise.application.dto.request.detail.OverallPromiseViewRequest4;
import timetogeter.context.promise.application.dto.request.detail.OverallPromiseviewRequest2;
import timetogeter.context.promise.application.dto.request.manage.CreatePromiseViewRequest1;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse1;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse2;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse3;
import timetogeter.context.promise.application.dto.response.detail.OverallPromiseViewResponse4;
import timetogeter.context.promise.application.dto.response.manage.CreatePromiseViewResponse1;
import timetogeter.context.promise.application.service.PromiseDetailInfoService;
import timetogeter.global.interceptor.response.error.dto.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promise")
@RequiredArgsConstructor
@Slf4j
public class PromiseDetailController {
    private final PromiseDetailInfoService promiseDetailInfoService;

//======================
// 사용자가 속한 그룹 내 약속을 보여주는 화면 (Step1,2,3,4)
//======================

    /*
    사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 - Step1

    [웹] 로그인 상태로 토큰 담아 요청 ->
    [서버] PromiseProxyUser에서 encPromiseId(개인키로 암호화한 약속 아이디) 반환
     */
    @PostMapping(value = "/overallPromise1", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<OverallPromiseViewResponse1>> overallPromise1(
            @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception{
        String userId = userPrincipal.getId();
        List<OverallPromiseViewResponse1> response = promiseDetailInfoService.getEncPromiseIdList(userId);
        return SuccessResponse.from(response);
    }

    /*
   사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 - Step2

   [웹] promiseId를 담아 요청 ->
   [서버] Promise, PromiseCheck 테이블에서 약속 확정 여부(정하는중),(확정완료) 약속 정보 반환
    */
    @PostMapping(value = "/overallPromise2", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<OverallPromiseViewResponse2>> overallPromise2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody OverallPromiseviewRequest2 request) throws Exception{
        String userId = userPrincipal.getId();
        List<OverallPromiseViewResponse2> response = promiseDetailInfoService.getPromiseInfoList(userId, request);
        return SuccessResponse.from(response);
    }


    /*
  사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 - Step3

   [웹] 개인키로 암호화한 약속키를 담아 요청 ->
   [서버] PromiseShareKey에서 encPromiseKey로 해당되는 scheduleId들을 반환
   */
    @PostMapping(value = "/overallPromise3", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<OverallPromiseViewResponse3>> overallPromise3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody OverallPromiseViewRequest3 request) throws Exception{
        String userId = userPrincipal.getId();
        List<OverallPromiseViewResponse3> response = promiseDetailInfoService.getScheduleIdList(userId, request);
        return SuccessResponse.from(response);
    }

    /*
사용자가 속한 그룹 내 약속을 (정하는중) , (확정완료) 로 구분지어 보여주는 화면 - Step4

[웹] 개인키로 암호화한 약속키를 담아 요청 ->
[서버] PromiseShareKey에서 encPromiseKey로 해당되는 scheduleId들을 반환
*/
    @PostMapping(value = "/overallPromise4", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<OverallPromiseViewResponse4>> overallPromise4(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody OverallPromiseViewRequest4 request) throws Exception{
        String userId = userPrincipal.getId();
        List<OverallPromiseViewResponse4> response = promiseDetailInfoService.getScheduleInfoList(userId, request);
        return SuccessResponse.from(response);
    }
}
