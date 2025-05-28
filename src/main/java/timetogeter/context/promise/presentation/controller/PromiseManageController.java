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
import timetogeter.context.promise.application.dto.request.CreatePromiseAlimRequest1;
import timetogeter.context.promise.application.dto.request.CreatePromiseViewRequest1;
import timetogeter.context.promise.application.dto.request.CreatePromiseViewRequest2;
import timetogeter.context.promise.application.dto.request.CreatePromiseViewRequest3;
import timetogeter.context.promise.application.dto.response.CreatePromiseAlimResponse1;
import timetogeter.context.promise.application.dto.response.CreatePromiseViewResponse1;
import timetogeter.context.promise.application.dto.response.CreatePromiseViewResponse2;
import timetogeter.context.promise.application.dto.response.CreatePromiseViewResponse3;
import timetogeter.context.promise.application.service.PromiseManageInfoService;
import timetogeter.global.interceptor.response.error.dto.SuccessResponse;

@RestController
@RequestMapping("/api/v1/promise")
@RequiredArgsConstructor
@Slf4j
public class PromiseManageController {
    private PromiseManageInfoService promiseManageInfoService;

//======================
// 약속 만들기 - 기본 정보 입력 "화면" 보여주기 (Step1,2,3)
//======================

    /*
    약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step1

    [웹] 그룹원이 userId, encGroupId(개인키로 암호화한 groupId)를 담은 request를 요청
        /api/v1/promise/createView1 ->
    [서버] GroupProxyUser의 userId에 해당하는 '개인키로 암호화된 그룹 아이디', '개인키로 암호화한
		  (그룹키로 암호화한 사용자 고유 아이디)'를 리스트 형태로 반환
     */
    @PostMapping(value = "/createView1", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreatePromiseViewResponse1> createPromiseView1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseViewRequest1 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseViewResponse1 response = promiseManageInfoService.createPromiseView1(userId,request);
        return SuccessResponse.from(response);
    }

    /*
    약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step2

    [웹] 개인키로 암호화된 그룹 아이디, 암호화된 (그룹키로 암호화된 사용자 고유 아이디) 복호화 후
			그룹 아이디(저장해두기), (그룹키로 암호화된 사용자 고유 아이디)를 리스트 형태로 담아 요청
			/api/v1/promise/createView2 ->
    [서버] 그룹 아이디, (그룹키로 암호화한 사용자 고유 아이디)에 해당하는
			GroupShareKey테이블 내 "개인키로 암호화한 그룹키" 넘김
    */
    @PostMapping(value = "/createView2", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreatePromiseViewResponse2> createPromiseView2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseViewRequest2 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseViewResponse2 response = promiseManageInfoService.createPromiseView2(userId,request);
        return SuccessResponse.from(response);
    }

    /*
    약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step3

    [웹] 개인키로 "개인키로 암호화한 그룹키" 복호화후 저장해 두었다가,
		이전 저장한 그룹 아이디를 GroupShareKey테이블 내 레코드 리스트 요청
		 /api/v1/promise/createView3
    [서버] 해당 그룹 아이디에 해당하는 레코드들 리스트로 반환
    */
    @PostMapping(value = "/createView3", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreatePromiseViewResponse3> createPromiseView3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseViewRequest3 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseViewResponse3 response = promiseManageInfoService.createPromiseView3(userId,request);
        return SuccessResponse.from(response);
    }


//======================
// 약속 만들기 - 기본 정보 입력 및 수락 알림 보내기 (Step1)
//======================

    /*
    약속 만들기 - 기본 정보 입력 및 수락 알림 보내기 Step1

    [웹] 그룹원이 userId, 주제, 목적, 약속 공개 여부, groupId, <encUserId(그룹키로 암호화한
        사용자 고유 아이디)의 약속 참여 여부를 담은 리스트>를 담은 request를 요청
        /api/v1/promise/create1 ->
    [서버] 1. Promise에 약속(임의) 저장
		  2. 각 예비 약속원 (encUserId로 판단) 에게 "약속에 참여하시겠습니까?"라는 수락알림 보냄.
				(각 약속원의 개인키로 "약속 공유키", promiseId(약속 아이디) 를 암호화해서
				서버에 저장해야하기때문) -> 비동기적으로 구현!!!(수락을 누르는 시점이 약속원마다 다름)
     */
    @PostMapping(value = "/create1", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreatePromiseAlimResponse1> createPromise1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseAlimRequest1 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseAlimResponse1 response = promiseManageInfoService.createPromise1(userId,request);
        return SuccessResponse.from(response);
    }


}
