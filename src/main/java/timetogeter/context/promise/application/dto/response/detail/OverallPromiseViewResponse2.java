package timetogeter.context.promise.application.dto.response.detail;

public record OverallPromiseViewResponse2(
    boolean isConfirmed, //약속 확정 여부 반환
    String promiseId, //약속 아이디
    String title, //약속 제목
    String type, //약속 유형
    String startDate, //약속 시작 날짜 (년, 월, 일)
    String endDate, //약속 끝난 날짜 (년, 월, 일)
    String managerId, //약소장
    String promiseImg //약속 사진


) {
}
