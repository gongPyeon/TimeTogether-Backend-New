package timetogeter.context.group.application.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class GroupShareKeyNotFoundException extends CustomException {
    public GroupShareKeyNotFoundException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
