package timetogeter.context.promise.application.dto.response.basic;

import java.util.List;

public record CreatePromise3Response(
        String groupId,
        String groupName,
        String groupImg,
        String managerId,
        List<String> encUserId
) {
}
