package timetogeter.global.security.infrastructure.oauth2.userInfo;

import java.util.Map;
import java.util.Optional;

public class KakaoOAuth2User extends OAuth2UserInfo{
    private final Map<String, Object> account;
    public KakaoOAuth2User(Map<String, Object> attributes) {
        super(attributes);
        account = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getOAuth2Id() {
        return Optional.ofNullable(attributes.get("id"))
                .map(String::valueOf)
                .orElseThrow(() -> new NullPointerException("[ERROR] 카카오 OAuth 아이디가 없습니다"));
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(account.get("email"))
                .map(String::valueOf)
                .orElseThrow(() -> new NullPointerException("[ERROR] 카카오 OAuth 이메일이 없습니다"));
    }

    @Override
    public String getName() {
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return Optional.ofNullable(profile.get("nickname"))
                .map(String::valueOf)
                .orElseThrow(() -> new NullPointerException("[ERROR] 카카오 OAuth 이름이 없습니다"));
    }
}
