package timetogeter.context.time.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.service.UserQueryService;
import timetogeter.context.promise.application.dto.TimeRange;
import timetogeter.context.promise.application.service.PromiseQueryService;
import timetogeter.context.time.application.dto.DailyTimeDTO;
import timetogeter.context.time.application.dto.TimeRangeDTO;
import timetogeter.context.time.application.dto.request.TimeSlotReqDTO;
import timetogeter.context.time.application.dto.response.TimeBoardResDTO;
import timetogeter.context.time.application.dto.response.UserTimeBoardResDTO;
import timetogeter.context.time.domain.repository.PromiseTimeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBoardService {

    private final PromiseQueryService promiseQueryService;
    private final UserQueryService userQueryService;
    private final PromiseTimeRepository promiseTimeRepository;

    public TimeBoardResDTO getTimeBoard(String promiseId) {
        TimeRange timeRangeByPromise = promiseQueryService.getTimeRange(promiseId);
        TimeRangeDTO timeRange = new TimeRangeDTO(timeRangeByPromise.startDate(), timeRangeByPromise.endDate());
        List<DailyTimeDTO> availableTimes = promiseTimeRepository.findAllWithDailyTimesByPromiseId(promiseId);

        return new TimeBoardResDTO(promiseId, timeRange, availableTimes);
    }

    public UserTimeBoardResDTO getUsersByTime(String promiseId, TimeSlotReqDTO reqDTO) {
        List<String> availableUsers = promiseTimeRepository.findUsersAtTime(promiseId, reqDTO.date(), reqDTO.time());

        List<String> names = userQueryService.getUserNamesById(reqDTO.userIds());
        List<String> unavailableUsers = new ArrayList<>(names);
        unavailableUsers.removeAll(availableUsers);

        return new UserTimeBoardResDTO(reqDTO.date(), reqDTO.time(), availableUsers, unavailableUsers);
    }
}
