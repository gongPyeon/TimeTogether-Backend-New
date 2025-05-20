package timetogeter.context.auth.infrastructure.external.oauth2.client;

import java.util.Map;

public interface OAuth2Client {
    String getAccessToken(String code, String redirectUri);
    Map<String, Object> getUserInfo(String accessToken);
}
