package timetogeter.context.group.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EditGroup1Request(
        @Schema(description = "그룹 식별자", example = "grp_12345", required = true)
        @NotBlank(message = "groupId는 필수입니다.")
        String groupId,

        @Schema(description = "암호화된 그룹 ID", example = "BASE64-encGroupId==", required = true)
        @NotBlank(message = "encGroupId는 필수입니다.")
        String encGroupId,

        @Schema(description = "수정할 그룹 이름", example = "새 그룹명")
        String groupName,

        @Schema(description = "수정할 그룹 이미지 URL", example = "https://example.com/image.png")
        String groupImg,

        @Schema(description = "그룹 설명", example = "스터디 그룹입니다.")
        String description
) {
}
