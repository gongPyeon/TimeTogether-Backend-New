package timetogeter.context.group.application.dto.response;

import java.util.List;

public record EditGroup3Response(
        String groupName,
        String groupExplain,
        String groupImg,
        List<String> encUserIdList
) {
}
