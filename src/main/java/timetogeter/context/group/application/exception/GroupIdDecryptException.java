package timetogeter.context.group.application.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class GroupIdDecryptException extends CustomException {
    public GroupIdDecryptException(BaseErrorCode status, String log) {
        super(status, log);
    }

}
