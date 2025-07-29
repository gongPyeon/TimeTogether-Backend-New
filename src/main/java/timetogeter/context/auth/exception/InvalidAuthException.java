package timetogeter.context.auth.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class InvalidAuthException extends CustomException {
    public InvalidAuthException(BaseErrorCode status, String log) {
        super(status, log);
    }
}