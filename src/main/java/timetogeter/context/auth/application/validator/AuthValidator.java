package timetogeter.context.auth.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import timetogeter.context.auth.application.exception.InvalidAuthException;
import timetogeter.context.auth.application.exception.RedisException;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.util.redis.RedisUtil;

import static timetogeter.global.security.util.DataUtil.REFRESH_HEADER;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    public void validateDuplicateId(String userId){
        if(userRepository.existsById(userId))
            throw new InvalidAuthException(BaseErrorCode.INVALID_ID_DUP, "[ERROR] 중복된 아이디입니다.");
    }

    public void validateRefreshToken(String userId, String providedToken) {
        String redisToken = redisUtil.get(REFRESH_HEADER + userId);

        if (redisToken == null) {
            throw new RedisException(BaseErrorCode.INVALID_TOKEN_REDIS, "[ERROR] Redis에 토큰이 없습니다.");
        }

        if (!providedToken.equals(redisToken)) {
            throw new RedisException(BaseErrorCode.MISMATCH_TOKEN_REDIS, "[ERROR] Redis에 존재하는 토큰과 일치하지 않습니다.");
        }
    }

}
