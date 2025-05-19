package timetogeter.global.security.infrastructure.oauth2.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import timetogeter.global.security.application.dto.ApiCommand;
import timetogeter.global.security.infrastructure.oauth2.factory.OAuth2RequestFactory;
import timetogeter.global.security.infrastructure.oauth2.parser.OAuth2ResponseParser;
import timetogeter.global.security.util.api.ApiService;

import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractOAuth2Client implements OAuth2Client{
    protected final ApiService apiService;
    protected final OAuth2RequestFactory requestFactory;
    protected final OAuth2ResponseParser parser;

    protected abstract String getClientId();
    protected abstract String getClientSecret();
    protected abstract String getReqUri();
    protected abstract String getUserInfoUri();

    @Override
    public String getAccessToken(String code, String redirectUri) {
        ApiCommand command = new ApiCommand(getClientId(), getClientSecret(), code, redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = requestFactory.create(command);
        ResponseEntity<Map> response = apiService.send(getReqUri(), HttpMethod.POST, request);

        return parser.extractAccessToken(response);
    }

    @Override
    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = requestFactory.create(accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = apiService.send(getUserInfoUri(), HttpMethod.GET, request);

        return response.getBody();
    }
}
