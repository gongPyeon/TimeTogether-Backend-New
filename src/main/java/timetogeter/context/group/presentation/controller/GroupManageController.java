package timetogeter.context.group.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.group.application.dto.request.*;
import timetogeter.context.group.application.dto.response.*;
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.context.group.application.service.GroupManageMemberService;
import timetogeter.global.interceptor.response.error.dto.SuccessResponse;
import timetogeter.global.security.application.vo.principal.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupManageController {

    private final GroupManageInfoService groupManageInfoService;
    private final GroupManageMemberService groupManageMemberService;
    private final GroupManageDisplayService groupManageDisplayService;


    /*
    그룹 관리 - 그룹 만들기
     */
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<CreateGroupResponseDto> createGroup(
            //@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateGroupRequestDto request) throws Exception{
        String managerId = "manager_id_1"; //userPrincipal.getUsername();
        CreateGroupResponseDto response = groupManageInfoService.createGroup(request, managerId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 초대 받기
     */
    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<JoinGroupResponseDto> joinGroup(
            //@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody JoinGroupRequestDto request) throws Exception{
        String invitedId = "testuser_id_1";  //userPrincipal.getUsername();
        JoinGroupResponseDto response = groupManageMemberService.joinGroup(request, invitedId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 메인
     */
    @PostMapping(value = "/view", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<List<ViewGroupsInResponseDto>> viewGroupsIn(
            //@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ViewGroupsInRequestDto request) throws Exception{
        String userId = "testuser_id_1";  //userPrincipal.getUsername();
        List<ViewGroupsInResponseDto> response = groupManageDisplayService.viewGroupsIn(request, userId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 관리 - 나가기
     */

    /*
    그룹 상세 - 그룹 정보 수정
     */
    @PostMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<EditGroupInfoResponseDto> editGroup(
            //@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody EditGroupInfoRequestDto request) throws Exception{
        String managerId = "manager_id_1"; //userPrincipal.getUsername();
        EditGroupInfoResponseDto response = groupManageInfoService.editGroup(request, managerId);
        return SuccessResponse.from(response);
    }

    /*
    그룹 상세 - 그룹 초대하기
     */
    @PostMapping(value = "/invite", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<InviteGroupInfoResponseDto> inviteGroup(
            //@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody InviteGroupInfoRequestDto request) throws Exception{
        String userId = "manager_id_1"; //userPrincipal.getUsername();
        InviteGroupInfoResponseDto response = groupManageMemberService.inviteGroup(request, userId);
        return SuccessResponse.from(response);
    }


}