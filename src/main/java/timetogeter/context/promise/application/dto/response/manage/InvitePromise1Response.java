package timetogeter.context.promise.application.dto.response.manage;

import java.util.List;

public record InvitePromise1Response(
        List<String> whichEmailIn,
        String message
) {
}
