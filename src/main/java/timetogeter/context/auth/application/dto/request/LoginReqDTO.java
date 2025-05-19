package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginReqDTO {
    @NotNull
    private String userId;
    @NotNull
    private String password;
}
