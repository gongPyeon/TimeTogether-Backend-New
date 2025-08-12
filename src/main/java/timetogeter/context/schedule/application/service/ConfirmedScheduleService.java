package timetogeter.context.schedule.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.promise.domain.entity.PromiseShareKey;
import timetogeter.context.promise.exception.PromiseNotFoundException;
import timetogeter.context.schedule.application.dto.PromiseDetailDTO;
import timetogeter.context.schedule.application.dto.request.ScheduleConfirmReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseResDTO;
import timetogeter.context.schedule.application.dto.request.GetPromiseBatchReqDTO;
import timetogeter.context.schedule.application.dto.response.PromiseDetailResDTO;
import timetogeter.context.schedule.application.dto.response.PromiseListResDTO;
import timetogeter.context.schedule.domain.entity.Schedule;
import timetogeter.context.promise.domain.repository.PromiseShareKeyRepository;
import timetogeter.context.schedule.exception.ScheduleNotFoundException;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;
import timetogeter.context.time.application.dto.response.TimeBoardResDTO;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfirmedScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PromiseShareKeyRepository promiseShareKeyRepository;


    public PromiseListResDTO getPromiseView(GetPromiseBatchReqDTO reqDTO) {
        List<Schedule> schedules = scheduleRepository.findByScheduleIdIn(reqDTO.scheduleIdList());
        List<PromiseResDTO> promiseResDTOList = schedules.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getPurpose()))
                .collect(Collectors.toList());

        return new PromiseListResDTO(promiseResDTOList);
    }
    public PromiseListResDTO getPromiseViewByGroup(String groupId, GetPromiseBatchReqDTO reqDTO) {
        List<Schedule> schedules = scheduleRepository.findAllByGroupIdAndScheduleIdIn(groupId, reqDTO.scheduleIdList());
        List<PromiseResDTO> promiseResDTOList = schedules.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getPurpose()))
                .collect(Collectors.toList());

        return new PromiseListResDTO(promiseResDTOList);
    }

    public PromiseDetailResDTO getPromiseDetailView(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(BaseErrorCode.SCHEDULE_NOT_FOUND, "[ERROR} " + scheduleId + "에 해당하는 일정이 존재하지 않습니다."));

        List<String> names = promiseShareKeyRepository.findByPromiseId(schedule.getScheduleId()).stream()
                .map(s -> s.getEncUserId())
                .collect(Collectors.toList());

        PromiseDetailDTO dto = scheduleRepository.findDetailByScheduleId(scheduleId);
        return new PromiseDetailResDTO(dto.scheduleId(), dto.title(), dto.type(), dto.placeName(), dto.groupName(), names);
    }

    public PromiseListResDTO searchPromiseView(String query, List<String> filter) {
        List<Schedule> result  = scheduleRepository.searchByQueryAndFilters(query, filter);

        List<PromiseResDTO> dtoList = result.stream()
                .map(s -> new PromiseResDTO(s.getScheduleId(), s.getTitle(), s.getPurpose()))
                .toList();

        return new PromiseListResDTO(dtoList);
    }

    @Transactional
    public void confirmSchedule(String groupId, ScheduleConfirmReqDTO reqDTO) {
        Schedule schedule = Schedule.of(reqDTO.scheduleId(), reqDTO.title(), "", reqDTO.purpose(), reqDTO.placeId(), groupId);
        scheduleRepository.save(schedule);

        List<PromiseShareKey> shareKeys = promiseShareKeyRepository.findByPromiseId(reqDTO.promiseId());
        if (shareKeys.isEmpty()) {
            throw new PromiseNotFoundException(BaseErrorCode.PROMISE_KEY_NOT_FOUND, "[ERROR] 약속 공유키 테이블을 찾을 수 없습니다.");
        }

        for (PromiseShareKey key : shareKeys) {
            key.updateScheduleId(reqDTO.scheduleId());
        }
        promiseShareKeyRepository.saveAll(shareKeys);
    }
}
