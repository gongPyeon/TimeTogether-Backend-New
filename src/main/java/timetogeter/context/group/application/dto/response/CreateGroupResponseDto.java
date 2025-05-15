package timetogeter.context.group.application.dto.response;

public record CreateGroupResponseDto(
        String groupId,
        String groupName,
        String groupImg,
        String managerId
) {}

