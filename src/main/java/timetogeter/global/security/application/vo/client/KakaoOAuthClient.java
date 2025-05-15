package timetogeter.global.security.application.vo.client;

import java.util.Map;

public class KakaoOAuthClient implements OAuth2Client{
    @Override
    public String getAccessToken(String code, String redirectUri) {
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String accessToken) {
        return null;
    }
}
