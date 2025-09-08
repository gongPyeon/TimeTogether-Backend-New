package timetogeter.context.promise.application.dto.request.manage;

public record JoinPromise1Request(
        String promiseId,
        String encPromiseId,
        String encPromiseMemberId,
        String encUserId,
        String encPromiseKey
){
}
