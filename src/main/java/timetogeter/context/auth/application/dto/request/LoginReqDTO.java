package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginReqDTO(
    @NotNull String userId,
    @NotNull String password
) {}
