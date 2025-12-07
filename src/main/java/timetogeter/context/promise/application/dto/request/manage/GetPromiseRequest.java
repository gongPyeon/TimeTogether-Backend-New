package timetogeter.context.promise.application.dto.request.manage;

public record GetPromiseRequest(
        String promiseId, //약속 아이디
        String encUserId //그룹키로 암호화한 사용자 아이디
) {
}
