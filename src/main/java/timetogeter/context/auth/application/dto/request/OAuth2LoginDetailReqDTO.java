package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;
import timetogeter.context.auth.domain.vo.Gender;

public record OAuth2LoginDetailReqDTO(
        String userId,
        String telephone,
        @NotNull String age,
        @NotNull Gender gender,
        @NotNull String wrappedDEK
) {
}
