package timetogeter.context.schedule.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.request.TimestampReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.application.dto.response.TimestampResDTO;
import timetogeter.context.schedule.application.service.TimeStampQueryService;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timestamp")
public class TimeStampController {
    private final TimeStampQueryService timeStampQueryService;

    @PostMapping("/get")
    public BaseResponse<Object> getTimeStampList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @RequestBody TimestampReqDTO reqDTO){
        String userId = userPrincipal.getId();
        TimestampResDTO dto = timeStampQueryService.getTimeStampList(userId, reqDTO);
        return new BaseResponse<>(dto);
    }
}
