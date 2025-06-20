package timetogeter.context.schedule.application.dto.request;

public record CalendarRewriteRequest1(
        String title,
        String content,
        String type,
        String place,
        String placeUrl,
        String startDateTime,
        String endDateTime,

        String encPromiseKey,
        String encUserId
) {
}
