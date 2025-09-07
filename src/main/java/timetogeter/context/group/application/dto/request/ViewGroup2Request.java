package timetogeter.context.group.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ViewGroup2Request(
        @Schema(description = "그룹 식별자", example = "grp_12345", required = true)
        @NotBlank(message = "groupId는 필수입니다.")
        String groupId,

        @Schema(description = "암호화된 encUserId", example = "BASE64-encUserId==", required = true)
        @NotBlank(message = "encGroupMemberId는 필수입니다.")
        String encGroupMemberId
) {
}
