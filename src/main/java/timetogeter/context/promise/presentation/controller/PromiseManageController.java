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

@RestController
@RequestMapping("/promise")
@RequiredArgsConstructor
@Slf4j
public class PromiseManageController {
    private final PromiseManageInfoService promiseManageInfoService;

//======================
// 약속 만들기 - 약속 만들고 초대하기 (Step1)
//======================
    /*
    약속 만들기 - 초대하기 Step1

    [웹] encGroupId(groupId를 개인키)로 암호화해서 groupId, encGroupId 요청
        /promise/invite1 ->
    [서버] GroupProxyUser테이블 내에서 encencGroupMemberId 반환
     */


    @PostMapping(value = "/invite1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<InvitePromise1Response> invitePromise1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody InvitePromise1Request request) throws Exception{
        InvitePromise1Response response = promiseManageInfoService.invitePromise1(request);
        return new BaseResponse<>(response);
    }

//======================
// 약속 만들기 - 참여하기 (Step1)
//======================
    /*
     약속 만들기 - 참여하기 Step1

    [웹] encencGroupMemberId를 개인키로 복호화(encUserId)한후, groupId, encUserId 요청
        /promise/join1 ->
    [서버] encGroupKey 반환
    [웹] 개인키로 encGroupKey복호화해서 groupKey얻음
    */
    @PostMapping(value = "/join1", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<JoinPromise1Response> joinPromise1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody JoinPromise1Request request) throws Exception{
        String userId = userPrincipal.getId();
        JoinPromise1Response response = promiseManageInfoService.joinPromise1(userId, request);
        return new BaseResponse<>(response);
    }


}
