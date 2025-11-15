package timetogeter.context.schedule.application.dto.response;

public record CalendarViewResponse2(
        String scheduleId,
        String title,
        String content,
        String purpose,
        String placeName,
        String placeAddr,
        String placeInfo
) {
}
