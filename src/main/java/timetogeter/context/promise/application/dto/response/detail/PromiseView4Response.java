package timetogeter.context.promise.application.dto.response.detail;

import io.swagger.v3.oas.annotations.media.Schema;

public record PromiseView4Response(
        @Schema(description = "약속 확정 여부", example = "true")
        boolean isConfirmed,

        @Schema(description = "스케줄 ID", example = "61a4c8e6-ea48-47d3-9523-9cf09dd6aae4")
        String scheduleId,

        @Schema(description = "확정된 시간", example = "20251129T1430-20251129T1630")
        String confirmedDateTime,

        @Schema(description = "제목", example = "61a4c8e6-ea48-47d3-9523-9cf09dd6aae4")
        String title,

        @Schema(description = "내용", example = "초콜릿다음엔?")
        String content,

        @Schema(description = "목적", example = "초콜릿초콜릿")
        String purpose,

        @Schema(description = "장소 이름", example = "독서실")
        String placeName,

        @Schema(description = "장소 정보", example = "공부하는곳")
        String placeInfo,

        @Schema(description = "그룹 ID", example = "d71ac3eb-fc61-4cff-92c7-478a0e092936")
        String groupId
) {
}
