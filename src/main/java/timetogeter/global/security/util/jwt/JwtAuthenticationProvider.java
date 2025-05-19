package timetogeter.global.security.util.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import timetogeter.global.security.application.service.CustomUserDetailService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;
    public Authentication getAuthentication(String token) {
        String userId = tokenProvider.validateToken(token);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        // 로깅할 때 필요할수도 있는데 일단 보안을 고려해 토큰을 노출시키지 않음 = ""
    }
}
