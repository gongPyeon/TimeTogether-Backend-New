package timetogeter.context.schedule.domain.repository.custom;

import io.lettuce.core.dynamic.annotation.Param;
import timetogeter.context.schedule.application.dto.TimestampDetail;
import timetogeter.context.schedule.domain.entity.TimeStamp;

import java.time.LocalDate;
import java.util.List;

public interface TimeStampRepositoryCustom {
    List<String> findTimeStampsByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate);

    List<String> findTimeStampsByUserIdAndTimeStamp(String userId, List<LocalDate> dates);

    List<TimestampDetail> findTimeStampsAndDateByUserIdAndTimeStamp(String userId, List<LocalDate> dates);
    
    List<String> findAllScheduleIdsByUserId(String userId);
}
