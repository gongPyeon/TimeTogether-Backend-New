package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserIdDTO {
    @NotNull
    private String userId;
}
