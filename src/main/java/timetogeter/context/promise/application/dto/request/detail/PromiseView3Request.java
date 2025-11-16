package timetogeter.context.promise.application.dto.request.detail;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PromiseView3Request(
        @Schema(
                description = "약속 ID 리스트",
                example = """
                [
                  "e3f971e9-0e41-48b2-bb2e-b7594b98e170"
                ]
                """
        )
        List<String> promiseIdList
) {
}
