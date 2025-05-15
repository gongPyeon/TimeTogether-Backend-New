package timetogeter.global.security.application.vo.client;

import java.util.Map;

public interface OAuth2Client {
    String getAccessToken(String code, String redirectUri);
    Map<String, Object> getUserInfo(String accessToken);
}
