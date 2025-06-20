package timetogeter.context.schedule.application.dto.request;

import java.util.List;

public record CalendarViewRequest2(
    List<String> scheduleIdList
) {
    }
