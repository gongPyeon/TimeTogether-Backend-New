package timetogeter.context.time.application.dto.response;

import timetogeter.context.time.application.dto.DailyTimeDTO;
import timetogeter.context.time.application.dto.TimeRangeDTO;

import java.util.List;

public record TimeBoardResDTO(String promiseId,
                           TimeRangeDTO timeRange,
                           List<DailyTimeDTO> availableTimes) {}
