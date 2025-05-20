package timetogeter.context.group.application.dto.request;

public record InviteGroupInfoRequestDto(
        String groupId,
        String personalMasterKey
) {
}
