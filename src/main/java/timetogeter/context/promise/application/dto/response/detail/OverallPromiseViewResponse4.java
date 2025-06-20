package timetogeter.context.promise.application.dto.response.detail;

import timetogeter.context.promise.domain.vo.PromiseType;

public record OverallPromiseViewResponse4(
        boolean isConfirmed, //확정 여부
        String scheduleId, //일정 id
        String title , //일정 제목
        String content, //일정 내용
        String purpose, //일정 유형
        int placeId, //일정 장소 아이디
        String groupId //그룹 아이디
) {
}
