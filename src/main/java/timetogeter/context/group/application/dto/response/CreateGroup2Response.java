package timetogeter.context.group.application.dto.response;

public record CreateGroup2Response(
        String groupId,
        String groupName,
        String groupExplain,
        String groupImg,
        String managerId
) {}

