package timetogeter.context.promise.application.dto.request;

import java.util.List;

public record DispersePromiseReqDTO(String promiseId,
                                    List<String> userIds) {
}
