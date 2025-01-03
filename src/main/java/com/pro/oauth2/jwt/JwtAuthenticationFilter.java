package com.pro.oauth2.jwt;

import com.pro.oauth2.entity.RefreshToken;
import com.pro.oauth2.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtService.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (token != null && jwtService.validateToken(token)) {
                Authentication authentication = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (ExpiredJwtException e){
            String email = e.getClaims().getSubject();
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(email);
            if (optionalRefreshToken.isPresent()) {
                String newToken = jwtService.generateTokenFromRefreshToken(optionalRefreshToken.get());
                log.info("newAccessToken = {}", newToken);
                Authentication authentication = jwtService.getAuthentication(newToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
