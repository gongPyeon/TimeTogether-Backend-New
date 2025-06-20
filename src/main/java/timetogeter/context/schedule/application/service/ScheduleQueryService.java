package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.promise.application.service.PromiseQueryService;
import timetogeter.context.schedule.application.dto.PromiseRangeDTO;
import timetogeter.context.schedule.domain.repository.ScheduleRepository;
import timetogeter.context.schedule.domain.repository.TimeStampRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final TimeStampRepository timeStampRepository;
    private final PromiseQueryService promiseQueryService;

    public List<String> getScheduleIds(String userId, String promiseId) {
        PromiseRangeDTO dto = promiseQueryService.getPromiseRange(promiseId);

        return timeStampRepository.findTimeStampsByUserIdAndDateRange(userId, dto.startDate(), dto.endDate());
    }
}
