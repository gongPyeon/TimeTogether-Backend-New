package timetogeter.context.auth.application.dto.request;

import jakarta.validation.constraints.NotNull;
import timetogeter.context.auth.domain.vo.Gender;

public record OAuth2LoginDetailReqDTO(
        String userId,
        String telephone,
        String age,
        Gender gender,
        String img,
        String imgIv,
        String emailIv,
        String phoneIv
) {
}