package timetogeter.global.security.infrastructure.oauth2.factory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import timetogeter.global.security.application.dto.ApiCommand;

@Component
public class OAuth2RequestFactory {
    public HttpEntity<MultiValueMap<String, String>> create(ApiCommand cmd) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", cmd.clientId());
        params.add("redirect_uri", cmd.redirectUri());
        params.add("code", cmd.code());
        params.add("client_secret", cmd.clientSecret());

        return new HttpEntity<>(params, headers);
    }

    public HttpHeaders create(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }
}
