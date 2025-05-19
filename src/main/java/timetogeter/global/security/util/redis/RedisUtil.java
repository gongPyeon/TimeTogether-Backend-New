package timetogeter.global.security.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import timetogeter.context.auth.application.exception.RedisException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil { // TODO: Redis 리팩토링

    private static final String BEARER_TYPE = "Bearer";
    private final String REFRESH_TOKEN_KEY = "refresh";

    @Value("${jwt.refresh.expiration}")
    private int REFRESH_TOKEN_MAXAGE;
    private final RedisTemplate<String, String> redisTemplate;

    // 리프레시 토큰 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                getRefreshTokenKey(userId),
                refreshToken,
                REFRESH_TOKEN_MAXAGE,
                TimeUnit.SECONDS
        );
    }

    // 리프레시 토큰 조회
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(getRefreshTokenKey(userId));
    }

    // 리프레시 토큰 삭제
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(getRefreshTokenKey(userId));
    }

    // 리프레시 토큰 존재 여부 확인
    public boolean hasRefreshToken(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRefreshTokenKey(userId)));
    }

    // 리프레시 토큰 만료 시간 갱신
    public void updateRefreshTokenExpiration(String userId) {
        String refreshToken = getRefreshToken(userId);
        if (refreshToken != null) {
            redisTemplate.expire(getRefreshTokenKey(userId), REFRESH_TOKEN_MAXAGE, TimeUnit.SECONDS);
        }
    }

    // 리프레시 토큰 키 생성
    private String getRefreshTokenKey(String userId) {
        return REFRESH_TOKEN_KEY + userId;
    }

    public void setBlackList(String token, String value, long expireSeconds) {
        String key = "BL:" + token;
        redisTemplate.opsForValue().set(
                key,
                value,
                Duration.ofSeconds(expireSeconds)
        );
    }

    public boolean isBlackListed(String token) {
        if (token.startsWith(BEARER_TYPE)) {
            token = token.substring(7);
        }
        String key = "BL:" + token;
        String value = redisTemplate.opsForValue().get(key);
        return value != null;
    }

    public Boolean getBoolean(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(userId));
    }

    public Integer getInt(String userId) {
        Object value = redisTemplate.opsForValue().get(userId);
        if (value == null) return 0;

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 문자열을 숫자로 변환할 수 없습니다.");
        }
    }

    public void delete(String s) {
        redisTemplate.delete(s);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, String.valueOf(value), timeout, unit);
    }

    public void set(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, String.valueOf(value), duration);
    }
}
