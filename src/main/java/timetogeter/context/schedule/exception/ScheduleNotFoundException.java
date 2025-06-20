package timetogeter.context.schedule.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class ScheduleNotFoundException extends CustomException {
    public ScheduleNotFoundException(BaseErrorCode status, String message) {
        super(status, message);
    }
}
