package timetogeter.context.group.application.dto.request;

public record LeaveGroup2Request(
        boolean isManager,
        String encGroupId

) {
}
