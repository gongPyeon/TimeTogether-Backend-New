package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuth2LoginReqDTO {
    @NotNull
    private String provider;
    @NotNull
    private String code;
    @NotNull
    private String redirectUri;
}
