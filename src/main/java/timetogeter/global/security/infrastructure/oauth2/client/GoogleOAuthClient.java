package timetogeter.global.security.infrastructure.oauth2.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import timetogeter.global.security.infrastructure.oauth2.factory.OAuth2RequestFactory;
import timetogeter.global.security.infrastructure.oauth2.parser.OAuth2ResponseParser;
import timetogeter.global.security.util.api.ApiService;

import java.util.Map;

public class GoogleOAuthClient extends AbstractOAuth2Client {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.req-uri}")
    private String reqUri;

    public GoogleOAuthClient(ApiService apiService, OAuth2RequestFactory requestFactory, OAuth2ResponseParser parser) {
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
}
