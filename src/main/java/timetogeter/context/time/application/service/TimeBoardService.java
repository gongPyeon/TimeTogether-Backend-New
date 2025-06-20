package timetogeter.context.time.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.promise.application.dto.TimeRange;
import timetogeter.context.promise.application.service.PromiseQueryService;
import timetogeter.context.time.application.dto.DailyTimeDTO;
import timetogeter.context.time.application.dto.TimeRangeDTO;
import timetogeter.context.time.application.dto.response.TimeBoardDTO;
import timetogeter.context.time.domain.repository.PromiseTimeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBoardService {

    private final PromiseQueryService promiseQueryService;
    private final PromiseTimeRepository promiseTimeRepository;

    public TimeBoardDTO getTimeBoard(String promiseId) {
        TimeRange timeRangeByPromise = promiseQueryService.getTimeRange(promiseId);
        TimeRangeDTO timeRange = new TimeRangeDTO(timeRangeByPromise.startDate(), timeRangeByPromise.endDate());
        List<DailyTimeDTO> availableTimes = promiseTimeRepository.findAllWithDailyTimesByPromiseId(promiseId);

        return new TimeBoardDTO(promiseId, timeRange, availableTimes);
    }
}
