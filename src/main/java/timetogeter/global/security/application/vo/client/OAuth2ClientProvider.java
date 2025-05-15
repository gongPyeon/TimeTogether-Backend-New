package timetogeter.global.security.application.vo.client;

import lombok.RequiredArgsConstructor;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.promise.domain.vo.PromiseType;

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
