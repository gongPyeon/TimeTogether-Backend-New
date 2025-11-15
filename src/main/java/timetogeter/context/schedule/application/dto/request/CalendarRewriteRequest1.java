package timetogeter.context.schedule.application.dto.request;

public record CalendarRewriteRequest1(
        String encStartTimeEndTime,
        String title,
        String content,
        String purpose,
        String placeName,
        String placeAddr,
        String placeInfo,

        String encPromiseKey,
        String encUserId
) {
}
