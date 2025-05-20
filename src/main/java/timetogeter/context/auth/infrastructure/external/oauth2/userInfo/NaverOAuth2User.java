package timetogeter.context.auth.infrastructure.external.oauth2.userInfo;

import java.util.Map;
import java.util.Optional;
public class NaverOAuth2User extends OAuth2UserInfo{
    public NaverOAuth2User(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getOAuth2Id() {
        return Optional.ofNullable(attributes.get("id"))
                .map(String::valueOf)
                .orElseThrow(() -> new NullPointerException("[ERROR] 네이버 OAuth Id가 없습니다"));
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attributes.get("email"))
                .map(String::valueOf)
                .orElseThrow(() -> new NullPointerException("[ERROR] 네이버 OAuth 이메일이 없습니다"));
    }

    @Override
    public String getName() {
        return Optional.ofNullable(attributes.get("nickname"))
                .map(String::valueOf)
                .orElseThrow(() -> new NullPointerException("[ERROR] 네이버 OAuth 이름이 없습니다"));
    }
}
