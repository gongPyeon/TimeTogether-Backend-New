package timetogeter.context.auth.infrastructure.external.oauth2.parser;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.context.auth.exception.AuthFailureException;

import java.util.Map;

@Component
public class OAuth2ResponseParser {
    public String extractAccessToken(ResponseEntity<Map> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("access_token");
        }
        throw new AuthFailureException(BaseErrorCode.INVALID_OAUTH_TOKEN, "[ERROR] 토큰 발급을 실패했습니다 : " + response.getStatusCode());
    }
}
