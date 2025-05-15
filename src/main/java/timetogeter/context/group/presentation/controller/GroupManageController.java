package timetogeter.context.group.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.group.application.dto.request.CreateGroupRequestDto;
import timetogeter.context.group.application.dto.response.CreateGroupResponseDto;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.global.interceptor.response.error.dto.SuccessResponseDto;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupManageController {

    private final GroupManageInfoService groupManageInfoService;

    /*
    그룹 관리 - 그룹 만들기
     */
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponseDto<CreateGroupResponseDto> createGroup(@RequestBody CreateGroupRequestDto request) throws Exception{
        String mockManagerId = "test-user-uuid"; // 실제 환경에서는 인증 정보에서 꺼냄
        CreateGroupResponseDto response = groupManageInfoService.createGroup(request, mockManagerId);
        return SuccessResponseDto.from(response);
    }

    /*
    그룹 관리 - 초대 받기
     */



}