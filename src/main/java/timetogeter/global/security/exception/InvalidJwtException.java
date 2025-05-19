package timetogeter.global.security.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class InvalidJwtException extends CustomException {
    public InvalidJwtException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
