package timetogeter.context.schedule.application.dto.response;

public record CalendarCreateResponse1(
        String scheduleId,
        String title,
        String content,
        String encStartTimeAndEndTime
) {
}
