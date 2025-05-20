package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record OAuth2LoginReqDTO(
    @NotNull String provider,
    @NotNull String code,
    @NotNull String redirectUri
){}
