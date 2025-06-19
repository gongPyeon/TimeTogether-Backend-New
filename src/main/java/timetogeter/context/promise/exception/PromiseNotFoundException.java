package timetogeter.context.promise.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class PromiseNotFoundException extends CustomException {
    public PromiseNotFoundException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
