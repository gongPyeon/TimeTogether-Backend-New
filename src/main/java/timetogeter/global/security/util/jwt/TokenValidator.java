package timetogeter.global.security.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
            // TODO: 예외처리: 토큰이 NULL 입니다.
        }
        return true;
    }

    public boolean validateLogout(String token){
        if (redisUtil.isBlackListed(token)) {
            // TODO: 예외처리: 블랙리스트 처리된 액세스 토큰 입니다.
        }
        return true;
    }
}
