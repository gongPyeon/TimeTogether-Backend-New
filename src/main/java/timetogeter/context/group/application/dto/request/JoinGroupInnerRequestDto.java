package timetogeter.context.group.application.dto.request;

public record JoinGroupInnerRequestDto(
        String groupId,
        String groupKey,
        String personalMasterKey
) {
}
