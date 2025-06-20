package timetogeter.context.place.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class PlaceNotFoundException extends CustomException {
    public PlaceNotFoundException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
