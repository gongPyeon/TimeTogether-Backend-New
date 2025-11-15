package timetogeter.context.schedule.application.dto.request;

import java.time.LocalDate;
import java.util.List;

public record CalendarViewRequest1(
        List<LocalDate> timeStampInfoList
) {
}
