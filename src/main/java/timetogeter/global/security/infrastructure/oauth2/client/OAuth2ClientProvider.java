package timetogeter.global.security.infrastructure.oauth2.client;

import lombok.RequiredArgsConstructor;
import timetogeter.context.auth.domain.vo.Provider;

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
        };
    }
}
