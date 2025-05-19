package timetogeter.global.security.infrastructure.oauth2.client;

import java.util.Map;

public interface OAuth2Client {
    String getAccessToken(String code, String redirectUri);
    Map<String, Object> getUserInfo(String accessToken);
}
