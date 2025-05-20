package timetogeter.context.group.application.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class GroupShareKeyException extends CustomException {
    public GroupShareKeyException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
