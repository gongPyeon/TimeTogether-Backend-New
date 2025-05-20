package timetogeter.context.group.application.dto.response;

import java.util.List;

public record ViewGroupsInResponseDto(
        String groupName,
        String groupImg,
        List<String> groupMembers
) {
}
