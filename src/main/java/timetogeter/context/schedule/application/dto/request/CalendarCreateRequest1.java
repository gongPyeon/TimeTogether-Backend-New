package timetogeter.context.schedule.application.dto.request;

public record CalendarCreateRequest1(
        String title,
        String content,
        String purpose,

        String placeName,
        String placeAddr,

        String encStartTimeAndEndTime,

        String encPromiseKey,
        String encUserId
) {
}
