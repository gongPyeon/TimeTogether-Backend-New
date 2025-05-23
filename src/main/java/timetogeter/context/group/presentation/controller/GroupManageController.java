package timetogeter.context.group.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.group.application.dto.request.*;
import timetogeter.context.group.application.dto.response.*;
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.context.group.application.service.GroupManageMemberService;
import timetogeter.global.interceptor.response.error.dto.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
@Slf4j
public class GroupManageController {

    private final GroupManageInfoService groupManageInfoService;
    private final GroupManageMemberService groupManageMemberService;
    private final GroupManageDisplayService groupManageDisplayService;

//======================
// 그룹 관리 - 그룹 메인 보기 (Step1,2,3)
//======================

    /*
    그룹 관리 - 그룹 메인 보기 - step1

    [웹] 그룹원이 userId를 담은 request를 요청 /api/v1/group/view1 ->
    [서버] GroupProxyUser의 userId에 해당하는 '개인키로 암호화된 그룹 아이디', '개인키로 암호화한
		 (그룹키로 암호화한 사용자 고유 아이디)'를 반환
     */
    @PostMapping(value = "/view1", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<ViewGroup1Response>> viewGroup1(
            @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception{
        String userId = userPrincipal.getId();
        List<ViewGroup1Response> response = groupManageDisplayService.viewGroup1(userId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 그룹 메인 보기 - step2

    [웹] 암호화된 그룹 아이디, 암호화된 (그룹키로 암호화된 사용자 고유 아이디)를 개인키로 복호화 후
			그룹 아이디(리스트 형태로 그대로 저장해두기), (그룹키로 암호화된 사용자 고유 아이디)를 리스트 형태로 담아 요청
			/api/v1/group/view2 ->
    [서버] 그룹 아이디, (그룹키로 암호화한 사용자 고유 아이디)에 해당하는
			GroupShareKey테이블 내 "개인키로 암호화한 그룹키"를 리스트 형태로 넘김
     */
    @PostMapping(value = "/view2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<ViewGroup2Response>> viewGroup2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<ViewGroup2Request> requests) throws Exception{
        List<ViewGroup2Response> response = groupManageDisplayService.viewGroup2(requests);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 그룹 메인 보기 - step3

    [웹] 개인키로 "개인키로 암호화한 그룹키" 복호화후 저장해 두었다가,
		이전 저장한 그룹 아이디를 GroupShareKey테이블 내 레코드 리스트 요청, 그룹 정보 요청 /api/v1/group/view3
		->
    [서버] 해당 그룹 아이디에 해당하는 레코드들 리스트로 반환 , <그룹 정보> 또한 리스트 형태로 반환
     */
    @PostMapping(value = "/view3", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<ViewGroup3Response>> viewGroup3(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<ViewGroup3Request> requests) throws Exception{
        List<ViewGroup3Response> response = groupManageDisplayService.viewGroup3(requests);
        return SuccessResponse.from(response);
    }

//======================
// 그룹 관리 - 그룹 만들기 (Step1,2)
//======================

    /*
    그룹 관리 - 그룹 만들기 - step1

    [웹] (예비 방장)그룹원이 Group정보를 담은 request를 요청 /api/v1/group/new1 ->
    [서버] Group에 request기반으로 저장, 저장한 후 생성된 Group의 아이디 프론트에 반환 ->
     */
    @PostMapping(value = "/new1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreateGroup1Response> createGroup1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateGroup1Request request) throws Exception{
        String userId = userPrincipal.getId();
        CreateGroup1Response response = groupManageInfoService.createGroup1(request,userId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 그룹 만들기 - step2

    [웹] 그룹키 자체적 생성후,
         GroupId와
         Group 아이디를 개인키로 암호화 한 것,
		 그룹키로 암호화한 사용자 아이디,
		 (그룹키로 암호화한 사용자 아이디)를 개인키로 암호화한 것,
		 개인키로 암호화한 그룹키 5개를 묶어 request로 요청 /api/v1/group/new2 ->
    [서버] 요청을 GroupProxyUser, Group, GroupShareKey에 저장
     */
    @PostMapping(value = "/new2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreateGroup2Response> createGroup2(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateGroup2Request request) throws Exception{
        String userId = userPrincipal.getId();
        CreateGroup2Response response = groupManageInfoService.createGroup2(request,userId);
        return SuccessResponse.from(response);
    }

//======================
// 그룹 관리 - 초대받기 (Step1)
//======================

    /*
    그룹 관리 - 초대 받기 - step1


    [웹] "http://localhost:8080/group/join?token=그룹키&그룹아이디&랜덤UUID"
			해당 url에서 랜덤UUID만 추출후
			랜덤 UUID,
			개인키로 암호화한 그룹키 encGroupKey,
			그룹키로 암호화한 사용자 고유 아이디 encUserId,
			개인키로 암호화한 그룹 아이디 encGroupId,
			개인키로 암호화한 encUserId - encencGroupMemberId
			보냄
		 /api/v1/group/join1
		 ->
    [서버] 랜덤 UUID 존재여부 확인후,
			GroupProxyUser, GroupShareKey테이블 내 정보 저장
			참여완료 메시지 보냄

     */
    @PostMapping(value = "/join1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<JoinGroup1Response> joinGroup1(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody JoinGroup1Request request) throws Exception{
        String userId = userPrincipal.getId();
        JoinGroup1Response response = groupManageMemberService.joinGroup1(request,userId);
        return SuccessResponse.from(response);
    }

//======================
// 그룹 관리 - 그룹 나가기
//======================

    /*
    그룹 관리 - 나가기 //TODO: 약속 테이블 관련 내용도 사라져야함
    */


}