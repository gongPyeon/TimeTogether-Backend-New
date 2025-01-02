package com.pro.oauth2.handler;

import com.pro.oauth2.dto.UserResponseDto;
import com.pro.oauth2.jwt.JwtService;
import com.pro.oauth2.lib.CookieUtils;
import com.pro.oauth2.repository.CookieAuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.pro.oauth2.repository.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.redirectUri}")
    private String redirectUri; // 수정하기
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    @Value("${jwt.access.expiration}")
    private int ACCESS_TOKEN_MAXAGE;
    @Value("${jwt.refresh.expiration}")
    private int REFRESH_TOKEN_MAXAGE;
    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("Authentication success!");
        // String targetUrl = determineTargetUrl(request, response, authentication);
        String targetUrl = "/login-test";
        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }
        setAccessTokenInCookie(response, authentication);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue); // 이게 필요가 있을지 고민하기

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("redirect URIs are not matched.");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        // String targetUrl = redirectUri.orElse("/hello");

        UserResponseDto.TokenInfo tokenInfo = jwtService.generateToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", tokenInfo.getAccessToken())
                .build().toUriString();
    }

    private void setAccessTokenInCookie(HttpServletResponse response, Authentication authentication) {
        //JWT 생성
        UserResponseDto.TokenInfo tokenInfo = jwtService.generateToken(authentication);
        CookieUtils.addCookie(response, ACCESS_TOKEN, tokenInfo.getAccessToken(), ACCESS_TOKEN_MAXAGE);

        response.setHeader("Authorization", BEARER+tokenInfo.getAccessToken());
        log.info("access = {}", tokenInfo.getAccessToken());
        log.info("refresh = {}", tokenInfo.getRefreshToken());
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);

        if (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }
}
