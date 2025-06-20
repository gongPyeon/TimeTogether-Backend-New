package timetogeter.context.auth.infrastructure.external.oauth2.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.exception.InvalidProviderException;

@Component
@RequiredArgsConstructor
public class OAuth2ClientProvider {
    private final GoogleOAuthClient googleOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final NaverOAuthClient naverOAuthClient;

    public OAuth2Client getClient(Provider provider) {
        return switch (provider) {
            case GOOGLE -> googleOAuthClient;
            case KAKAO -> kakaoOAuthClient;
            case NAVER -> naverOAuthClient;
            default -> throw new InvalidProviderException("[ERROR] " + provider + "는 지원하지 않는 플랫폼입니다.");
        };
    }
}
