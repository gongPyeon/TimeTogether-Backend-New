package timetogeter.context.group.application.dto.response;

public record LeaveGroup1Response(
        String groupId,
        String message,
        Boolean isManager
) {
}
