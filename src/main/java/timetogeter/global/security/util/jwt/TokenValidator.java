package timetogeter.global.security.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.context.auth.application.exception.InvalidJwtException;
import timetogeter.global.common.util.redis.RedisUtil;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private static final String BEARER_TYPE = "Bearer";
    private final RedisUtil redisUtil;
    public boolean validateToken(String token) {
        return validateNotNull(token) && validateLogout(token);
    }

    public boolean validateNotNull(String token){
        if(!Objects.nonNull(token)){
            return false;
        }
        return true;
    }

    public boolean validateLogout(String token){
        if (token.startsWith(BEARER_TYPE)) {
            token = token.substring(7);
        }

        if (redisUtil.getBoolean("BL:" + token)) {
            throw new InvalidJwtException(BaseErrorCode.INVALID_TOKEN, "[ERROR] 블랙리스트 처리된 액세스 토큰 입니다.");
        }
        return true;
    }
}
