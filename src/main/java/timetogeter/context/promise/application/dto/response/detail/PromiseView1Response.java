package timetogeter.context.promise.application.dto.response.detail;

import io.swagger.v3.oas.annotations.media.Schema;

public record PromiseView1Response(
        @Schema(description = "개인키로 암호화된 약속 아이디", example = "0cL0PM....==")
        String encPromiseId
) {
}
