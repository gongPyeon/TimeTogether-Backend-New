package timetogeter.global.security.infrastructure.oauth2.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import timetogeter.global.security.infrastructure.oauth2.factory.OAuth2RequestFactory;
import timetogeter.global.security.infrastructure.oauth2.parser.OAuth2ResponseParser;
import timetogeter.global.security.util.api.ApiService;

import java.util.Map;

@Component
public class NaverOAuthClient extends AbstractOAuth2Client {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String reqUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUri;

    public NaverOAuthClient(ApiService apiService, OAuth2RequestFactory requestFactory, OAuth2ResponseParser parser) {
        super(apiService, requestFactory, parser);
    }

    @Override
    protected String getClientId() {
        return clientId;
    }

    @Override
    protected String getClientSecret() {
        return clientSecret;
    }

    @Override
    protected String getReqUri() {
        return reqUri;
    }

    @Override
    protected String getUserInfoUri() {
        return userInfoUri;
    }
}
