package timetogeter.context.schedule.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseDetailResDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.application.service.ConfirmedScheduleService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleQueryController {

    private final ConfirmedScheduleService confirmedScheduleService;

    // TODO: 페이징 처리는 batch 요청을 하는 프론트에서 제어, 날짜도 프론트에서 제어
    // 스케줄 아이디를 만들 수 있다고 가정
    @PostMapping("/promise/get")
    public BaseResponse<Object> getPromiseView(@RequestBody GetPromiseBatchReqDTO reqDTO){
        PromiseListResDTO dto = confirmedScheduleService.getPromiseView(reqDTO);
        return new BaseResponse<>(dto);
    }

    // TODO: 스케줄 아이디에 groupId 정보를 넣는다 vs entity에 넣는다
    // TODO: 추가해도 해당 그룹에 속한 사람이 누군지 확인할 수 없으므로 괜찮다고 판단
    // 스케줄 아이디를 만들 수 있다고 가정
    @PostMapping("/promise/get/{groupId}")
    public BaseResponse<Object> getPromiseView(
            @PathVariable String groupId,
            @RequestBody GetPromiseBatchReqDTO reqDTO){
        PromiseListResDTO dto = confirmedScheduleService.getPromiseViewByGroup(groupId, reqDTO);
        return new BaseResponse<>(dto);
    }

    // 약속 공유키가 존재한다고 가정
    @PostMapping("/promise/get/{scheduleId}/detail")
    public BaseResponse<Object> getPromiseView(
            @PathVariable String scheduleId){
        PromiseDetailResDTO dto = confirmedScheduleService.getPromiseDetailView(scheduleId);
        return new BaseResponse<>(dto);
    }
}
