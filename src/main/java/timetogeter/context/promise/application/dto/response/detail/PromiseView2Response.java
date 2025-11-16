package timetogeter.context.promise.application.dto.response.detail;

import io.swagger.v3.oas.annotations.media.Schema;

public record PromiseView2Response(

    @Schema(description = "약속 확정 여부", example = "false")
    boolean isConfirmed, //약속 확정 여부 반환
    @Schema(description = "약속 ID", example = "e3f971e9-0e41-48b2-bb2e-b7594b98e170")
    String promiseId, //약속 아이디
    @Schema(description = "약속 제목", example = "초콜릿모임")
    String title, //약속 제목
    @Schema(description = "약속 타입", example = "스터디")
    String type, //약속 유형
    @Schema(description = "시작 날짜", example = "2025-11-14")
    String startDate, //약속 시작 날짜 (년, 월, 일)
    @Schema(description = "종료 날짜", example = "2025-11-19")
    String endDate, //약속 끝난 날짜 (년, 월, 일)
    @Schema(description = "약속 생성자 ID", example = "makerid")
    String managerId, //약소장
    @Schema(description = "약속 대표 이미지", example = "빼빼로만들기-초콜릿")
    String promiseImg //약속 사진


) {
}
