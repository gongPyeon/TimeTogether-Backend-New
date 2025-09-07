package timetogeter.context.group.application.dto.request;

public record LeaveGroup3Request(
        String groupId,
        boolean isManager,
        String encUserId,
        String encencGroupMemberId
) {
}
