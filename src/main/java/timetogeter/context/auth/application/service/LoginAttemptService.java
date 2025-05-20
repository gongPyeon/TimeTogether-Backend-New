package timetogeter.context.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.exception.AuthFailureException;
import timetogeter.global.security.util.redis.RedisUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static timetogeter.global.security.util.DataUtil.*;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final RedisUtil redisUtil;

    public void increaseFailedAttempts(String userId) {
        try {
            String failKey = FAIL_HEADER + userId;
            String lockKey = LOCK_HEADER + userId;
            Integer failCount = Integer.parseInt(redisUtil.get(failKey)) + 1;
            redisUtil.set(failKey, failCount, 1, TimeUnit.HOURS);

            Duration lockDuration = getLockDurationByFailCount(failCount);
            if (lockDuration != null) {
                redisUtil.set(lockKey, LOCKED, lockDuration);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 문자열을 숫자로 변환할 수 없습니다.");
        } catch (RedisConnectionFailureException e) {
            throw new AuthFailureException(BaseErrorCode.REDIS_ERROR, "[ERROR] 로그아웃 시도횟수(실패) 저장에 실패했습니다.");
        }
    }

    public boolean isLocked(String userId) {
        Boolean locked = redisUtil.getBoolean(LOCK_HEADER + userId);
        return locked != null && locked;
    }

    public void resetFailedAttempts(String userId) {
        try {
            redisUtil.delete(FAIL_HEADER + userId);
            redisUtil.delete(LOCK_HEADER + userId);
        } catch (RedisConnectionFailureException e) {
            throw new AuthFailureException(BaseErrorCode.REDIS_ERROR, "[ERROR] 로그아웃 시도횟수(실패) 초기화에 실패했습니다.");
        }
    }

    private Duration getLockDurationByFailCount(int failCount) {
        switch (failCount) {
            case 5: return Duration.ofMinutes(5);
            case 6: return Duration.ofMinutes(10);
            case 7: return Duration.ofMinutes(30);
            case 8: return Duration.ofHours(1);
            default:
                return (failCount > 8) ? Duration.ofHours(2) : null;
        }
    }
}