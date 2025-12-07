package timetogeter.context.time.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class TimeNotFoundException extends CustomException {
    public TimeNotFoundException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
