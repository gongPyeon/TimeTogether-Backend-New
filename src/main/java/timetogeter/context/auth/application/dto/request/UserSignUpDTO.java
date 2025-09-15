package timetogeter.context.auth.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import timetogeter.context.auth.domain.vo.Gender;

@Schema(requiredProperties = {"userId","email","password","nickname"})
public record UserSignUpDTO(
        @NotNull String userId,
        @NotNull String email,
        @NotNull String password,
        @NotNull String nickname,
        @NotNull String wrappedDEK,
        String telephone,
        String age,
        Gender gender
){}
