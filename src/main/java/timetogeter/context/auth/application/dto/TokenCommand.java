package timetogeter.context.auth.application.dto;

public record TokenCommand(
        String accessToken,
        int accessTokenExpirationTime,
        String refreshToken,
        int refreshTokenExpirationTime
){}