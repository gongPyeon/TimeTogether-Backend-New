package timetogeter.context.promise.application.dto.response.detail;

import io.swagger.v3.oas.annotations.media.Schema;

public record PromiseView3Response(
        @Schema(description = "스케줄 ID", example = "61a4c8e6-ea48-47d3-9523-9cf09dd6aae4")
        String scheduleId
) {
}
