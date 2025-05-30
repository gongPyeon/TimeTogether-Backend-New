package timetogeter.context.promise.application.dto.request;

public record CreateJoinPromiseRequest4(
        String encPromiseId,
        String encPromiseMemberId,
        String encUserId,
        String encPromiseKey
) {
}
