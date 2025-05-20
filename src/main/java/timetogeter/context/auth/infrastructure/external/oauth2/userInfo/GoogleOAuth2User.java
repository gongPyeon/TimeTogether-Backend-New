package timetogeter.context.auth.infrastructure.external.oauth2.userInfo;

import java.util.Map;
import java.util.Optional;

public class GoogleOAuth2User extends OAuth2UserInfo{
    public GoogleOAuth2User(Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getOAuth2Id() {
        return Optional.ofNullable(attributes.get("sub"))
                .map(String::valueOf)  // 값이 있으면 String 변환
                .orElseThrow(() -> new NullPointerException("[ERROR] 구글 OAuth Id가 없습니다"));
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attributes.get("email"))
                .map(String::valueOf)
                .orElseThrow(()-> new NullPointerException("[ERROR] 구글 OAuth 이메일이 없습니다"));
    }

    @Override
    public String getName() {
        return Optional.ofNullable(attributes.get("name"))
                .map(String::valueOf)
                .orElseThrow(()-> new NullPointerException("[ERROR] 구글 OAuth 이름이 없습니다"));
    }
}
