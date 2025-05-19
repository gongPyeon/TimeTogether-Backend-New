package timetogeter.context.auth.application.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class RedisException extends CustomException {
    public RedisException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
