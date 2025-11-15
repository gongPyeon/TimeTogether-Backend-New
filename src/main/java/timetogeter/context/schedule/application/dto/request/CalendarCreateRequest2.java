package timetogeter.context.schedule.application.dto.request;

import java.time.LocalDate;

public record CalendarCreateRequest2(
        String encStartTimeAndEndTime,
        LocalDate timeStampInfo
) {
}

