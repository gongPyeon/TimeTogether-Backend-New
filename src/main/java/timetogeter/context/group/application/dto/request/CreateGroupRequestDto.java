package timetogeter.context.group.application.dto.request;

public record CreateGroupRequestDto(
        String groupName,
        String groupImg,
        String personalMasterKey
) {}
