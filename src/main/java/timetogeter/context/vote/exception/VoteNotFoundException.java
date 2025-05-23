package timetogeter.context.vote.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class VoteNotFoundException extends CustomException {
    public VoteNotFoundException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
