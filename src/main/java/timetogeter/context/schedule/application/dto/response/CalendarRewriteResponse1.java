package timetogeter.context.schedule.application.dto.response;

public record CalendarRewriteResponse1(
        String title,
        String content,
        String type,
        String place,
        String placeUrl,
        String startDateTime,
        String endDateTime
) {
}
