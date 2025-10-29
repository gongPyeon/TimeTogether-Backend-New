package timetogeter.context.schedule.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.schedule.application.dto.request.ScheduleConfirmReqDTO;
import timetogeter.context.schedule.application.service.ConfirmedScheduleService;
import timetogeter.context.time.application.dto.response.TimeBoardResDTO;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ConfirmedScheduleService confirmedScheduleService;
    @PostMapping("/confirm/{groupId}")
    public BaseResponse<Object> confirmSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable("groupId") String groupId,
                                                @RequestBody ScheduleConfirmReqDTO reqDTO) {
        String userId = userPrincipal.getId();
        confirmedScheduleService.confirmSchedule(userId, groupId, reqDTO);
        return new BaseResponse<>(BaseCode.SUCCESS_SCHEDULE_UPDATE);
    }
}
