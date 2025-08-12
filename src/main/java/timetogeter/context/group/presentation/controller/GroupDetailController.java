package timetogeter.context.group.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.group.application.dto.request.*;
import timetogeter.context.group.application.dto.response.*;
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.context.group.application.service.GroupManageMemberService;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.SuccessResponse;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
@Slf4j
public class GroupDetailController {
    private final GroupManageInfoService groupManageInfoService;
    private final GroupManageMemberService groupManageMemberService;
    private final GroupManageDisplayService groupManageDisplayService;

//======================
// 그룹 상세 - 그룹 정보 수정 (Step1,2,3)
//======================
    /*
    그룹 상세 - 그룹 정보 수정 - step1

    [웹] 방장이 userId와 개인키로 암호화한 그룹 아이디(encGroupId), 그룹 아이디,
			수정하려는 request를 요청
			/api/v1/group/edit1 ->
    [서버] Group내 groupId 레코드들중 managerId와 userId가 동등한게 있는 경우,
			수정하려는 request반영해서 저장.
			GroupProxyUser테이블 내에서 userId,encGroupId에 해당하는
			encencGroupMemberId반환
    */
    @PostMapping(value = "/edit1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<EditGroup1Response> editGroup1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody EditGroup1Request request) throws Exception{
        String managerId = userPrincipal.getId();
        EditGroup1Response response = groupManageInfoService.editGroup1(request, managerId);
        return new BaseResponse<>(response);
    }

    /*
    그룹 상세 - 그룹 정보 수정 - step2

    [웹] encencGroupMemberId를 개인키로 복호화한 후(encUserId = encGroupMemberId),
		groupId, encUserId를 보냄 /api/v1/group/edit2->
    [서버] GroupShareKey테이블 내에서 groupId, encUserId에 해당하는
			레코드의 encGroupKey를 반환
    */
    @PostMapping(value = "/edit2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<EditGroup2Response> editGroup2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody EditGroup2Request request) throws Exception{
        EditGroup2Response response = groupManageInfoService.editGroup2(request);
        return new BaseResponse<>(response);
    }

    /*
    그룹 상세 - 그룹 정보 수정 - step3

    [웹] 개인키로 encGroupKey를 복호화한 후 그룹키 저장, 그룹 아이디 보냄
			/api/v1/group/edit3 ->
    [서버] GroupShareKey테이블 내 groupId로 찾은
			encUserId 리스트, 그룹 아이디에 따른 그룹 정보 반환
    */
    @PostMapping(value = "/edit3", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<EditGroup3Response> editGroup3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody EditGroup3Request request) throws Exception{
        EditGroup3Response response = groupManageInfoService.editGroup3(request);
        return new BaseResponse<>(response);
    }


//======================
// 그룹 상세 - 그룹 초대하기 (Step1,2,3)
//======================

    /*
    그룹 상세 - 그룹 초대하기 - step1

    [웹] 그룹원이 groupId, 개인키로 암호화한 그룹 아이디를 보냄 /api/v1/group/invite1 ->
    [서버] GroupProxyUser테이블 내 encencGroupMemberId 반환 ->
     */
    @PostMapping(value = "/invite1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<InviteGroup1Response> inviteGroup1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody InviteGroup1Request request) throws Exception{
        String userId = userPrincipal.getId();
        InviteGroup1Response response = groupManageMemberService.inviteGroup1(request,userId);
        return new BaseResponse<>(response);
    }

    /*
    그룹 상세 - 그룹 초대하기 - step2

    [웹] 개인키로 encencGroupMemberId 복호화해서 encUserId 얻고,
		encUserId, groupId 로 encGroupKey 요청 /api/v1/group/invite2 ->
    [서버] GroupShareKey테이블 내 encGroupKey 반환->
    */
    @PostMapping(value = "/invite2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<InviteGroup2Response> inviteGroup2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody InviteGroup2Request request) throws Exception{
        InviteGroup2Response response = groupManageMemberService.inviteGroup2(request);
        return new BaseResponse<>(response);
    }

    /*
    그룹 상세 - 그룹 초대하기 - step3

    [웹] 개인키로 그룹키 획득,
		enc ( 그룹키, 그룹아이디, 랜덤 UUID(해당 초대코드가 유효하다는 증거), 초대하려는 userId ) by 랜덤 UUID, 
		생성해서 랜덤 UUID
		
		위 2개의 값 보냄
		/api/v1/group/invite3 ->
    [서버] 받은 enc ( ... ) by 랜덤 UUID, 랜덤 UUID redis에 INVITE_KEY:enc:[ ]:UUID:[ ]로 저장
    */
    @PostMapping(value = "/invite3", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<InviteGroup3Response> inviteGroup3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody InviteGroup3Request request) throws Exception{
        InviteGroup3Response response = groupManageMemberService.inviteGroup3(request);
        return new BaseResponse<>(response);
    }
}
