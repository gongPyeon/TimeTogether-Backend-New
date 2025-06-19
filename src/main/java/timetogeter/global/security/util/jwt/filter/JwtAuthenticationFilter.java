package timetogeter.global.security.util.jwt.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import timetogeter.global.security.util.jwt.JwtAuthenticationProvider;
import timetogeter.global.security.util.jwt.TokenValidator;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String HEADER = "Authorization";
    private final JwtAuthenticationProvider authenticationProvider;
    private final TokenValidator tokenValidator;


    /**내가 임의로 추가한 부분*
    // 화이트리스트 경로
    private final List<RequestMatcher> whiteList = List.of(
            antMatcher(GET, "/docs/**"),
            antMatcher(POST, "/static/docs/**"),
            antMatcher(POST, "/auth/**"),
            antMatcher(GET, "/test/**"),
            antMatcher(POST, "/api/v1/group/**")
    );*/


    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String accessToken = tokenValidator.extract(request.getHeader(HEADER));

        if(tokenValidator.validateToken(accessToken)) {
            Authentication authentication = authenticationProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(req, res);
    }
}
