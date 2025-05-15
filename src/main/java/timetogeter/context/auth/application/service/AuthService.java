package timetogeter.context.auth.application.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.dto.request.LoginReqDTO;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.exception.InvalidAuthException;
import timetogeter.context.auth.application.validator.AuthValidator;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.application.dto.TokenCommand;
import timetogeter.global.security.util.jwt.JwtTokenProvider;
import timetogeter.global.security.util.redis.RedisUtil;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;
    private final RedisUtil redisUtil;

    @Transactional
    public String signUp(UserSignUpDTO dto) {
        User user = new User(dto);
        authValidator.validateDuplicateId(dto.getUserId());

        userRepository.save(user);
        return BaseCode.SUCCESS_SIGN_UP.getMessage();
    }

    public String reissueToken(String refreshToken) {
        String userId = jwtTokenProvider.validateToken(refreshToken);
        authValidator.validateRefreshToken(userId, refreshToken);

        String accessToken = jwtTokenProvider.generateTokenFromRefreshToken(refreshToken);
        return accessToken;
    }

    public TokenCommand login(LoginReqDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword()));
        TokenCommand token = jwtTokenProvider.generateToken(authentication);

        redisUtil.saveRefreshToken(dto.getUserId(), token.getRefreshToken());

        return token;
    }
}
