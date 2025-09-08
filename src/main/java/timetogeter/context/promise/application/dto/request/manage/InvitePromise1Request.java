package timetogeter.context.promise.application.dto.request.manage;

import java.util.List;
import java.util.Map;

public record InvitePromise1Request(
        List<Map<String, Integer>> whichUserIdIn
) {
}
