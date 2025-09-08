package timetogeter.context.group.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EditGroup2Request(
        @Schema(description = "그룹 식별자", example = "grp_12345", required = true)
        @NotBlank(message = "groupId는 필수입니다.")
        String groupId,

        @Schema(description = "그룹 내 사용자 식별자(복호화 결과)", example = "BASE64-encUserId==", required = true)
        @NotBlank(message = "encUserId는 필수입니다.")
        String encUserId
) {
}
