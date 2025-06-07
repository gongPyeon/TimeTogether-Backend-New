package timetogeter.context.group.application.exception;

import timetogeter.global.interceptor.response.error.CustomException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

public class GroupProxyUserNotFoundException extends CustomException {
  public GroupProxyUserNotFoundException(BaseErrorCode status, String log) {
    super(status, log);
  }

}
