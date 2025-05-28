package timetogeter.context.promise.application.dto.response;

import java.util.List;

public record CreatePromiseViewResponse3(
        String groupId,
        String groupName,
        String groupImg,
        String managerId,
        List<String> encUserId
) {
}
