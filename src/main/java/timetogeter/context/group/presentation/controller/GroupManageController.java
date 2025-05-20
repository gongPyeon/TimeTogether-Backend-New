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


    /*
    그룹 관리 - 그룹 만들기
     */
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreateGroupResponseDto> createGroup(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateGroupRequestDto request) throws Exception{
        String managerId = userPrincipal.getId();
        log.info("방장 아이디 : " + managerId);
        CreateGroupResponseDto response = groupManageInfoService.createGroup(request, managerId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 초대 받기
     */
    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<JoinGroupResponseDto> joinGroup(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody JoinGroupRequestDto request) throws Exception{
        String invitedId = userPrincipal.getId();
        JoinGroupInnerRequestDto req = groupManageMemberService.getRequestDto(request);
        JoinGroupResponseDto response = groupManageMemberService.joinGroup(req, invitedId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 메인
     */
    @PostMapping(value = "/view", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<ViewGroupsInResponseDto>> viewGroupsIn(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ViewGroupsInRequestDto request) throws Exception{
        String userId = userPrincipal.getId();
        List<ViewGroupsInResponseDto> response = groupManageDisplayService.viewGroupsIn(request, userId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 나가기 //TODO: 약속 테이블 관련 내용도 사라져야함
     */

    /*
    그룹 상세 - 그룹 정보 수정
     */
    @PostMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<EditGroupInfoResponseDto> editGroup(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody EditGroupInfoRequestDto request) throws Exception{
        String managerId = userPrincipal.getId();
        EditGroupInfoResponseDto response = groupManageInfoService.editGroup(request, managerId);
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