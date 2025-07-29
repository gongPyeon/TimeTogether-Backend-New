package timetogeter.context.schedule.domain.repository.custom;

import timetogeter.context.schedule.application.dto.PromiseDetailDTO;
import timetogeter.context.schedule.application.dto.response.PromiseDetailResDTO;
import timetogeter.context.schedule.domain.entity.Schedule;

import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> searchByQueryAndFilters(String query, List<String> filters);
    PromiseDetailDTO findDetailByScheduleId(String scheduleId);
}
