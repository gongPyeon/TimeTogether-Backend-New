package timetogeter.context.place.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class InvalidPlaceInfoException extends CustomException {
    public InvalidPlaceInfoException(BaseErrorCode status, String log) {
        super(status, log);
    }
}
