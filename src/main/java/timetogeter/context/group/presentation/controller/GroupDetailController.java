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
import timetogeter.context.group.application.dto.request.EditGroupInfoRequestDto;
import timetogeter.context.group.application.dto.request.InviteGroupInfoRequestDto;
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
