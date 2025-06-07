package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.schedule.application.dto.PromiseResDTO;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.infrastructure.repository.ScheduleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfirmedScheduleService {

    private final ScheduleRepository scheduleRepository;


    public PromiseListResDTO getPromiseView(GetPromiseBatchReqDTO reqDTO) {
        List<Schedule> schedules = scheduleRepository.findAllByIdIn(reqDTO.scheduleIdList());
        List<PromiseResDTO> promiseResDTOList = schedules.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getType()))
                .collect(Collectors.toList());

        return new PromiseListResDTO(promiseResDTOList);
    }
    public PromiseListResDTO getPromiseViewByGroup(String groupId, GetPromiseBatchReqDTO reqDTO) {
        List<Schedule> schedules = scheduleRepository.findAllByGroupIdAndIdIn(groupId, reqDTO.scheduleIdList());
        List<PromiseResDTO> promiseResDTOList = schedules.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getType()))
                .collect(Collectors.toList());

        return new PromiseListResDTO(promiseResDTOList);
    }

}
