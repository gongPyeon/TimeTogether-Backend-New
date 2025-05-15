package timetogeter.global.security.util.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;
import timetogeter.global.interceptor.response.StatusCode;

import java.io.IOException;

public class ResponseUtil {
    public static void handleException(StatusCode status, HttpServletResponse response) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        ErrorResponse baseErrorResponse = ErrorResponse.of(status).getBody();
        response.setStatus(status.getCode());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(
                objectMapper.writeValueAsString(baseErrorResponse)
        );
    }
}
