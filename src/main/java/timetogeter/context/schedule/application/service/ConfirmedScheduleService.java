package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.group.application.service.GroupInfoService;
import timetogeter.context.schedule.application.dto.PromiseResDTO;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseDetailResDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.schedule.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.schedule.exception.ScheduleNotFoundException;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfirmedScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupInfoService groupInfoService;
    private final PromiseShareKeyRepository promiseShareKeyRepository;


    public PromiseListResDTO getPromiseView(GetPromiseBatchReqDTO reqDTO) {
        List<Schedule> schedules = scheduleRepository.findAllByScheduleIdIn(reqDTO.scheduleIdList());
        List<PromiseResDTO> promiseResDTOList = schedules.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getType()))
                .collect(Collectors.toList());

        return new PromiseListResDTO(promiseResDTOList);
    }
    public PromiseListResDTO getPromiseViewByGroup(String groupId, GetPromiseBatchReqDTO reqDTO) {
        List<Schedule> schedules = scheduleRepository.findAllByGroupIdAndScheduleIdIn(groupId, reqDTO.scheduleIdList());
        List<PromiseResDTO> promiseResDTOList = schedules.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getType()))
                .collect(Collectors.toList());

        return new PromiseListResDTO(promiseResDTOList);
    }

    public PromiseDetailResDTO getPromiseDetailView(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(BaseErrorCode.SCHEDULE_NOT_FOUND, "[ERROR} " + scheduleId + "에 해당하는 일정이 존재하지 않습니다."));

        String groupName = groupInfoService.getGroupName(schedule.getGroupId());
        // TODO: PromiseId는 별도의 테이블에서 관리해야함 -> Schedule에서 Promise에 접근할 수 있도록
        List<String> names = promiseShareKeyRepository.findByPromiseId(schedule.getPromiseId()).stream()
                .map(s -> s.getEncUserId())
                .collect(Collectors.toList());

        return new PromiseDetailResDTO(scheduleId, schedule.getTitle(), schedule.getType(), schedule.getPlace(), groupName, names);
    }

    public PromiseListResDTO searchPromiseView(String query, List<String> filter) {
        List<Schedule> result = scheduleRepository.searchByQueryAndFilters(query, filter);

        List<PromiseResDTO> dtoList = result.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getType()))
                .toList();

        return new PromiseListResDTO(dtoList);
    }
}
