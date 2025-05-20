package timetogeter.context.place.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class PlaceUserIdNotSame extends CustomException {
    public PlaceUserIdNotSame(BaseErrorCode status, String log) {
        super(status, log);
    }
}
