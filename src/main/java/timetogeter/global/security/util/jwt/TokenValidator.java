package timetogeter.global.security.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.exception.InvalidJwtException;
import timetogeter.global.security.util.redis.RedisUtil;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final RedisUtil redisUtil;
    public boolean validateToken(String token) {
        return validateNotNull(token) && validateLogout(token);
    }

    public boolean validateNotNull(String token){
        if(!Objects.nonNull(token)){
            return false;
            // throw new InvalidJwtException(BaseErrorCode.INVALID_TOKEN, "[ERROR] 토큰이 NULL 입니다.");
        }
        return true;
    }

    public boolean validateLogout(String token){
        if (redisUtil.isBlackListed(token)) {
            throw new InvalidJwtException(BaseErrorCode.INVALID_TOKEN, "[ERROR] 블랙리스트 처리된 액세스 토큰 입니다.");
        }
        return true;
    }
}
