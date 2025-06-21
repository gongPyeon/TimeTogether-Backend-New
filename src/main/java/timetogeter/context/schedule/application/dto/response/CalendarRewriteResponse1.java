package timetogeter.context.schedule.application.dto.response;

public record CalendarRewriteResponse1(
        String encStartTimeAndEndTime,
        String title,
        String content,
        String purpose,
        String placeName,
        String placeAddr
) {
}
