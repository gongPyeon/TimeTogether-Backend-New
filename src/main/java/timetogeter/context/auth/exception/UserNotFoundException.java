package timetogeter.context.auth.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
