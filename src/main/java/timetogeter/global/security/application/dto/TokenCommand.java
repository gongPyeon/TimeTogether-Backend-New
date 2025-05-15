package timetogeter.global.security.application.dto;

public record TokenCommand(String accessToken,
                           int accessTokenExpirationTime,
                           String refreshToken,
                           int refreshTokenExpirationTime){

}
