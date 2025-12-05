package timetogeter.context.group.application.dto.response;
import java.util.*;

public record ViewGroup3Response(
        String groupId,
        String groupName,
        String groupImg,
        String explanation,
        String managerId,
        List<String> encUserId
) {
}
