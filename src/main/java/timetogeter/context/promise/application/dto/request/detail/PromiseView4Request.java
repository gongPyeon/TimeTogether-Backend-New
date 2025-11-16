package timetogeter.context.promise.application.dto.request.detail;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PromiseView4Request(
        @Schema(
                description = "스케줄 ID 리스트",
                example = """
                [
                  "61a4c8e6-ea48-47d3-9523-9cf09dd6aae4"
                ]
                """
        )
        List<String> scheduleIdList
) {
}
