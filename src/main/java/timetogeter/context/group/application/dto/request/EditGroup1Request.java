package timetogeter.context.group.application.dto.request;

public record EditGroup1Request(
        String groupId,
        String encGroupId,
        String groupName,
        String groupImg,
        String explain
) {
}
