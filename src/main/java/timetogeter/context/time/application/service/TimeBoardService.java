package timetogeter.context.time.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.service.UserQueryService;
import timetogeter.context.auth.exception.UserNotFoundException;
import timetogeter.context.promise.application.dto.TimeRange;
import timetogeter.context.promise.application.dto.response.PromiseRegisterDTO;
import timetogeter.context.promise.application.service.PromiseConfirmService;
import timetogeter.context.promise.application.service.PromiseQueryService;
import timetogeter.context.time.application.dto.DailyTimeDTO;
import timetogeter.context.time.application.dto.TimeRangeDTO;
import timetogeter.context.time.application.dto.request.TimeSlotReqDTO;
import timetogeter.context.time.application.dto.response.TimeBoardResDTO;
import timetogeter.context.time.application.dto.response.UserScheduleResDTO;
import timetogeter.context.time.application.dto.response.UserTimeBoardResDTO;
import timetogeter.context.time.domain.repository.PromiseTimeRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBoardService {

    private final PromiseQueryService promiseQueryService;
    private final UserQueryService userQueryService;
    private final PromiseConfirmService promiseConfirmService;
    private final PromiseTimeRepository promiseTimeRepository;

    public TimeBoardResDTO getTimeBoard(String promiseId) {
        TimeRange timeRangeByPromise = promiseQueryService.getTimeRange(promiseId);
        TimeRangeDTO timeRange = new TimeRangeDTO(timeRangeByPromise.startDate());
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

    @Transactional
    public PromiseRegisterDTO confirmDateTime(String userId, String promiseId, String dateTime) {
        boolean isConfirmed = promiseConfirmService.confirmedPromiseManager(userId, promiseId);
        if(!isConfirmed) throw new UserNotFoundException(BaseErrorCode.PROMISE_MANGER_FORBIDDEN, "[ERROR] 사용자에게 약속장 권한이 없습니다.");

        promiseConfirmService.confirmPromiseDateTime(promiseId, dateTime);

        return promiseConfirmService.confirmedScheduleByTime(promiseId);
    }
}
