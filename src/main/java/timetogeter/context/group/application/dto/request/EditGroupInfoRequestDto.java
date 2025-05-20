package timetogeter.context.group.application.dto.request;

public record EditGroupInfoRequestDto(
        String groupId,
        String personalMasterKey,
        String groupName,
        String groupImg
) {
}
