package timetogeter.global.security.exception.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.exception.InvalidJwtException;

import java.io.IOException;

import static timetogeter.global.security.util.response.ResponseUtil.handleException;

@Component
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(req, res);
        }catch (JwtException | AuthenticationException e){
            log.error("[ERROR] " + e.getMessage());
            handleException(BaseErrorCode.INVALID_TOKEN, res);
        }catch (AccessDeniedException e){
            log.error("[ERROR] " + e.getMessage());
            handleException(BaseErrorCode.INVALID_USER, res);
        }catch (Exception e){
            log.error("[ERROR] 알 수 없는 서버오류입니다 : " + e.getMessage());
            handleException(BaseErrorCode.INTERNAL_SERVER_ERROR, res);
        }
    }
}
