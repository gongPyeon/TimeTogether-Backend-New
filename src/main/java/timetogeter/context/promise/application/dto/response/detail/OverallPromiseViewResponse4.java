package timetogeter.context.promise.application.dto.response.detail;

import timetogeter.context.promise.domain.vo.PromiseType;

public record OverallPromiseViewResponse4(
        boolean isConfirmed, //확정 여부
        String scheduleId, //일정 id
        String title , //일정 제목
        String content, //일정 내용
        String type, //일정 유형
        String place, //일정 장소
        String placeUrl //일정 url
) {
}
