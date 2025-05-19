package timetogeter.context.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.global.security.util.redis.RedisUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final RedisUtil redisUtil;

    public void increaseFailedAttempts(String userId) {
        String failKey = "fail:" + userId;
        String lockKey = "lock:" + userId;
        Integer failCount = redisUtil.getInt(failKey) + 1;

        redisUtil.set(failKey, failCount, 1, TimeUnit.HOURS);

        Duration lockDuration = getLockDurationByFailCount(failCount);
        if (lockDuration != null) {
            redisUtil.set(lockKey, "LOCKED", lockDuration);
        }
    }

    public boolean isLocked(String userId) {
        Boolean locked = redisUtil.getBoolean("lock:" + userId);
        return locked != null && locked;
    }

    public void resetFailedAttempts(String userId) {
        redisUtil.delete("fail:" + userId);
        redisUtil.delete("lock:" + userId);
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