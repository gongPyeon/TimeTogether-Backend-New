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
import timetogeter.context.promise.application.dto.request.manage.*;
import timetogeter.context.promise.application.dto.response.manage.*;
import timetogeter.context.promise.application.service.PromiseManageInfoService;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.SuccessResponse;

@RestController
@RequestMapping("/api/v1/promise")
@RequiredArgsConstructor
@Slf4j
public class PromiseManageController {
    private final PromiseManageInfoService promiseManageInfoService;

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
    public BaseResponse<CreatePromiseViewResponse1> createPromiseView1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseViewRequest1 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseViewResponse1 response = promiseManageInfoService.createPromiseView1(userId,request);
        return new BaseResponse<>(response);
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
    public BaseResponse<CreatePromiseViewResponse2> createPromiseView2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseViewRequest2 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseViewResponse2 response = promiseManageInfoService.createPromiseView2(userId,request);
        return new BaseResponse<>(response);
    }

    /*
    약속 만들기 - 기본 정보 입력 "화면" 보여주기 Step3

    [웹] 개인키로 "개인키로 암호화한 그룹키" 복호화후 저장해 두었다가,
		이전 저장한 그룹 아이디를 GroupShareKey테이블 내 레코드 리스트 요청
		 /api/v1/promise/createView3
    [서버] 해당 그룹 아이디에 해당하는 레코드들 리스트로 반환
    */
    @PostMapping(value = "/createView3", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromiseViewResponse3> createPromiseView3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseViewRequest3 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseViewResponse3 response = promiseManageInfoService.createPromiseView3(userId,request);
        return new BaseResponse<>(response);
    }


//======================
// 약속 만들기 - 약속 만들고 알림 보내기 (Step1,2,3,4,5)
//======================

    /*
    약속 만들기 - 약속 만들고 알림 보내기 Step1

    [웹] encGroupId(groupId를 개인키)로 암호화해서 groupId, encGroupId 요청
        /api/v1/promise/create1 ->
    [서버] GroupProxyUser테이블 내에서 encencGroupMemberId 반환
     */
    @PostMapping(value = "/create1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromiseAlimResponse1> createPromise1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseAlimRequest1 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseAlimResponse1 response = promiseManageInfoService.createPromise1(userId,request);
        return new BaseResponse<>(response);
    }

    /*
     약속 만들기 - 약속 만들고 알림 보내기 Step2

    [웹] encencGroupMemberId를 개인키로 복호화(encUserId)한후, groupId, encUserId 요청
        /api/v1/promise/create2 ->
    [서버] encGroupKey 반환
    [웹] 개인키로 encGroupKey복호화해서 groupKey얻음
    */
    @PostMapping(value = "/create2", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromiseAlimResponse2> createPromise2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseAlimRequest2 request) throws Exception{
        CreatePromiseAlimResponse2 response = promiseManageInfoService.createPromise2(request);
        return new BaseResponse<>(response);
    }

    /*
    약속 만들기 - 약속 만들고 알림 보내기 Step3

    [웹] 그룹원이 userId, 주제, 목적, 약속 공개 여부, groupId, 약속장인사람 = managerId(plain
        user id) = 그룹키로 복호화한 약속장 아이디 , <encUserId(그룹키로 암호화한
        사용자 고유 아이디)의 약속 참여 여부 리스트>를 담은 request를 요청

        약속 공유키 랜덤 생성(저장)
        /api/v1/promise/create3 ->
    [서버] Promise에 약속(임의) 저장해서 약속 아이디 반환
     */
    @PostMapping(value = "/create3", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromiseAlimResponse3> createPromise3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseAlimRequest3 request) throws Exception{
        CreatePromiseAlimResponse3 response = promiseManageInfoService.createPromise3(request);
        return new BaseResponse<>(response);
    }

    /*
    약속 만들기 - 약속 만들고 알림 보내기 Step4

    [웹] encUserId(그룹키로 암호화한 사용자 고유 아이디)리스트 , groupId로
		encGroupKey(개인키로 암호화한 그룹키)리스트 요청
		/api/v1/promise/create4 ->
    [서버] encGroupKey(개인키로 암호화한 그룹키)리스트 반환
     */
    @PostMapping(value = "/create4", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromiseAlimResponse4> createPromise4(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseAlimRequest4 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseAlimResponse4 response = promiseManageInfoService.createPromise4(request);
        return new BaseResponse<>(response);
    }

    /*
    약속 만들기 - 약속 만들고 알림 보내기 Step5

    [웹] encPromiseId, encPromiseMemberId, encUserId, encPromiseKey, encPromiseKey2,
        <encUserId(그룹키로 암호화한 사용자 고유 아이디)의 약속 참여 여부 리스트> 를 담아서 요청
        /api/v1/promise/create4 ->
    [서버]
		1. 약속을 만든 사람은 자동 동의한 것으로 간주하고, PromiseProxyUser, PromiseShareKey
				에 저장
		2. 각 예비 약속원 (encUserId로 판단) 에게 "약속에 참여하시겠습니까?"라는 수락알림
				약속 공유키 암호화해서 같이 보냄.
					(각 약속원의 개인키로 "약속 공유키", promiseId(약속 아이디) 를 암호화해서
						서버에 저장해야하기때문) -> 비동기적으로 구현!!!(수락을 누르는 시점이 약속원마다 다름)
						PromiseProxyUser에 저장, promiseShareKey에 저장
     */
    @PostMapping(value = "/create5", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreatePromiseAlimResponse5> createPromise5(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreatePromiseAlimRequest5 request) throws Exception{
        String userId = userPrincipal.getId();
        CreatePromiseAlimResponse5 response = promiseManageInfoService.createPromise5(userId,request);
        return new BaseResponse<>(response);
    }

//======================
// 약속 만들기 - 약속 참여 알림 수락 (Step1,2,3,4)
//======================


    /*
      약속 만들기 - 약속 참여 알림 수락 Step1

    [웹] encGroupId(groupId를 개인키)로 암호화해서 groupId, encGroupId 요청
        /api/v1/promise/create/join1 ->
    [서버] GroupProxyUser테이블 내에서 encencGroupMemberId 반환
     */
       @PostMapping(value = "/create/join1", produces = MediaType.APPLICATION_JSON_VALUE)
       public BaseResponse<CreateJoinPromiseResponse1> createJoinPromise1(
               @AuthenticationPrincipal UserPrincipal userPrincipal,
               @RequestBody CreateJoinPromiseRequest1 request) throws Exception{
           String userId = userPrincipal.getId();
           CreateJoinPromiseResponse1 response = promiseManageInfoService.createJoinPromise1(userId,request);
           return new BaseResponse<>(response);
       }


    /*
      약속 만들기 - 약속 참여 알림 수락 Step2

    [웹] encencGroupMemberId를 개인키로 복호화(encUserId)한후, groupId, encUserId 요청
        /api/v1/promise/create/join2 ->
    [서버] encGroupKey 반환
    [웹] 개인키로 encGroupKey복호화해서 groupKey얻음(저장)
     */
    @PostMapping(value = "/create/join2", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreateJoinPromiseResponse2> createJoinPromise2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateJoinPromiseRequest2 request) throws Exception{
        CreateJoinPromiseResponse2 response = promiseManageInfoService.createJoinPromise2(request);
        return new BaseResponse<>(response);
    }

    /*
   약속 만들기 - 약속 참여 알림 수락 Step3

    [웹] encUserId(groupKey로 userId 암호화)를 담아 요청
        /api/v1/promise/create/join3 ->
    [서버] redis에 promise:notify:%s:%s", encUserId, "PENDING" 이거 있으면, 해당 value 반환
    */
    @PostMapping(value = "/create/join3", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreateJoinPromiseResponse3> createJoinPromise3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateJoinPromiseRequest3 request) throws Exception{
        CreateJoinPromiseResponse3 response = promiseManageInfoService.createJoinPromise3(request);
        return new BaseResponse<>(response);
    }

       /*
      약속 만들기 - 약속 참여 알림 수락 Step4

      [웹] promiseId, encGroupKey(개인키로 암호화한 그룹키)로 value 복호화해서 promiseKey 얻음
           andandand
           encPromiseId(현재 사용자), encPromiseMemberId(현재 사용자),
           encUserId(현재 사용자), encPromiseKey(현재 사용자)
           를 담아 요청 /api/v1/promise/create/join4 ->
       [서버] PromiseShareKey, PromiseProxyUser 테이블에 저장
       */
    @PostMapping(value = "/create/join4", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<CreateJoinPromiseResponse4> createJoinPromise4(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateJoinPromiseRequest4 request) throws Exception{
        String userId = userPrincipal.getId();
        CreateJoinPromiseResponse4 response = promiseManageInfoService.createJoinPromise4(userId,request);
        return new BaseResponse<>(response);
    }

}
