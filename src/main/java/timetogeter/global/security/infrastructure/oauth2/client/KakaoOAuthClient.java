package timetogeter.global.security.infrastructure.oauth2.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import timetogeter.global.security.application.dto.ApiCommand;
import timetogeter.global.security.infrastructure.oauth2.factory.OAuth2RequestFactory;
import timetogeter.global.security.infrastructure.oauth2.parser.OAuth2ResponseParser;
import timetogeter.global.security.util.api.ApiService;

import java.util.Map;

@Component
public class KakaoOAuthClient extends AbstractOAuth2Client{

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String reqUri;

    public KakaoOAuthClient(ApiService apiService, OAuth2RequestFactory requestFactory, OAuth2ResponseParser parser) {
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
