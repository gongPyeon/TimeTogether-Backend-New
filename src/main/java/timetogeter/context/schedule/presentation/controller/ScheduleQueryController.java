package timetogeter.context.schedule.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.service.ConfirmedScheduleService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleQueryController {

    private final ConfirmedScheduleService confirmedScheduleService;

    // TODO: 페이징 처리는 batch 요청을 하는 프론트에서 제어
    @PostMapping("/promise/get")
    public BaseResponse<Object> getPromiseView(@RequestBody GetPromiseBatchReqDTO reqDTO){
        confirmedScheduleService.getPromiseView(reqDTO);
        return new BaseResponse<>(BaseCode.SUCCESS_LOGIN);
    }
}
