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
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import timetogeter.global.security.util.jwt.JwtAuthenticationProvider;
import timetogeter.global.security.util.jwt.TokenValidator;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String HEADER = "Authorization";
    private final JwtAuthenticationProvider authenticationProvider;
    private final TokenValidator tokenValidator;


    //화이트리스트 경로
    private final List<RequestMatcher> whiteList = List.of(
            antMatcher(GET, "/docs/**"),
            antMatcher(POST, "/static/docs/**"),
            antMatcher(POST, "/auth/sign-up"),
            antMatcher(POST, "/auth/oauth2/login"),
            antMatcher(POST, "/auth/login"),
            antMatcher(GET, "/test/**"),
            //antMatcher(POST, "/api/v1/group/**"),
            antMatcher(GET, "/actuator/**")
    );


    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        // 화이트리스트 경로는 인증 없이 통과
        if (whiteList.stream().anyMatch(matcher -> matcher.matches(request))) {
            filterChain.doFilter(req, res);
            return;
        }
        String accessToken = tokenValidator.extract(request.getHeader(HEADER));

        if(tokenValidator.validateToken(accessToken)) {
            Authentication authentication = authenticationProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(req, res);
    }
}
