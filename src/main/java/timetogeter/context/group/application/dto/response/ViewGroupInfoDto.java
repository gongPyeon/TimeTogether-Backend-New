package timetogeter.context.group.application.dto.response;

public record ViewGroupInfoDto(
        String groupId,
        String groupName,
        String groupImg,
        String managerId
) {
}
