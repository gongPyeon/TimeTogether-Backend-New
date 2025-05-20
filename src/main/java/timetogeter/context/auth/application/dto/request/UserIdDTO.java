package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;
public record UserIdDTO (
    @NotNull String userId
){}
