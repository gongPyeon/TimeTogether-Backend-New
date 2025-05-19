package timetogeter.global.security.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 접근을 제한해야할지 생각해보기
public class TokenCommand {
    private String accessToken;
    private int accessTokenExpirationTime;
    private String refreshToken;
    private int refreshTokenExpirationTime;
}