package timetogeter.context.group.application.dto.request;

public record JoinGroupRequestDto(
        String groupId,
        String groupKey,
        String personalMasterKey
) {
}
