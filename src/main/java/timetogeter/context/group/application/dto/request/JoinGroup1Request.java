package timetogeter.context.group.application.dto.request;

public record JoinGroup1Request(
        String randomUUID,
        String groupId,
        String encGroupKey,
        String encUserId,
        String encGroupId,
        String encencGroupMemberId
) {
}
