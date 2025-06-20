package timetogeter.context.schedule.application.dto.response;

public record CalendarCreateResponse1(
        String title,
        String content,
        String startDateTime,
        String endDateTime
) {
}
