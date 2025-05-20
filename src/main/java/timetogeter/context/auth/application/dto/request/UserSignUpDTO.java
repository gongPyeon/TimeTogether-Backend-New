package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;
import timetogeter.context.auth.domain.vo.Gender;
public record UserSignUpDTO(
    @NotNull String userId,
    @NotNull String email,
    @NotNull String password,
    @NotNull String nickname,
    String telephone,
    String age,
    Gender gender
){}
