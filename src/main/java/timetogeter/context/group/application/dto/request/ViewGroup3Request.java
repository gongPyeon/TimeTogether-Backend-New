package timetogeter.context.group.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ViewGroup3Request(
        @Schema(description = "그룹 식별자", example = "grp_12345", required = true)
        @NotBlank(message = "groupId는 필수입니다.")
        String groupId
) {
}
