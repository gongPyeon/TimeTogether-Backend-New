package timetogeter.context.schedule.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseDetailResDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.application.service.ConfirmedScheduleService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promise")
public class ScheduleQueryController {

    private final ConfirmedScheduleService confirmedScheduleService;

    @PostMapping("/get")
    public BaseResponse<Object> getPromiseView(@RequestBody GetPromiseBatchReqDTO reqDTO){
        PromiseListResDTO dto = confirmedScheduleService.getPromiseView(reqDTO);
        return new BaseResponse<>(dto);
    }

    @PostMapping("/get/{groupId}")
    public BaseResponse<Object> getPromiseView(
            @PathVariable("groupId") String groupId,
            @RequestBody GetPromiseBatchReqDTO reqDTO){
        PromiseListResDTO dto = confirmedScheduleService.getPromiseViewByGroup(groupId, reqDTO);
        return new BaseResponse<>(dto);
    }

    @GetMapping("/get/{scheduleId}/detail")
    public BaseResponse<Object> getPromiseDetailView(
            @PathVariable("scheduleId") String scheduleId){
        PromiseDetailResDTO dto = confirmedScheduleService.getPromiseDetailView(scheduleId);
        return new BaseResponse<>(dto);
    }

    @GetMapping("/search")
    public BaseResponse<Object> searchPromiseView(
            @RequestParam("query") String query, @RequestParam(required = false, value = "filter") List<String> filter){
        PromiseListResDTO dto = confirmedScheduleService.searchPromiseView(query, filter);
        return new BaseResponse<>(dto);
    }


}
