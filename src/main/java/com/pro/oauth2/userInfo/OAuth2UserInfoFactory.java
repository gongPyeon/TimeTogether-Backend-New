package com.pro.oauth2.userInfo;

import com.pro.oauth2.enums.SocialType;
import com.pro.oauth2.jwt.JwtService;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes){
        switch(socialType){
            case GOOGLE : return new GoogleOAuth2User(attributes);
            case NAVER : return new NaverOAuth2User(attributes);
            case KAKAO : return new KakaoOAuth2User(attributes);

            default : throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
