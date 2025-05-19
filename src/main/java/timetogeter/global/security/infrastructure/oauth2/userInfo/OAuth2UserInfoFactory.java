package timetogeter.global.security.infrastructure.oauth2.userInfo;

import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.global.security.exception.InvalidProviderException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(Provider socialType, Map<String, Object> attributes){
        switch(socialType){
            case GOOGLE : return new GoogleOAuth2User(attributes);
            case NAVER : return new NaverOAuth2User(attributes);
            case KAKAO : return new KakaoOAuth2User(attributes);

            default : throw new InvalidProviderException("[ERROR] 지원하지 않은 소셜 플랫폼입니다.");
        }
    }
}
