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
import timetogeter.context.group.application.dto.request.EditGroup1Request;
import timetogeter.context.group.application.dto.request.InviteGroupInfoRequestDto;
import timetogeter.context.group.application.dto.response.EditGroup1Response;
import timetogeter.context.group.application.dto.response.EditGroupInfoResponseDto;
import timetogeter.context.group.application.dto.response.InviteGroupInfoResponseDto;
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.context.group.application.service.GroupManageMemberService;
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
    public SuccessResponse<EditGroup1Response> editGroup(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody EditGroup1Request request) throws Exception{
        String managerId = userPrincipal.getId();
        EditGroup1Response response = groupManageInfoService.editGroup1(request, managerId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 상세 - 그룹 초대하기
     */
    @PostMapping(value = "/invite", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<InviteGroupInfoResponseDto> inviteGroup(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody InviteGroupInfoRequestDto request) throws Exception{
        String userId = userPrincipal.getId();
        InviteGroupInfoResponseDto response = groupManageMemberService.inviteGroup(request, userId);
        return SuccessResponse.from(response);
    }
}
