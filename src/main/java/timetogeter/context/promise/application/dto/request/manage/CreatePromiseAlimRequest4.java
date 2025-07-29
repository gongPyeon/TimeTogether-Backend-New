package timetogeter.context.promise.application.dto.request.manage;
import java.util.*;

public record CreatePromiseAlimRequest4(
        List<String> encUserIdList,
        String groupId
) {
}
