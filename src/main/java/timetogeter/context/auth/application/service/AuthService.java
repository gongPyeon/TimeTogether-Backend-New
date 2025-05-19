package timetogeter.context.auth.application.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.dto.request.LoginReqDTO;
import timetogeter.context.auth.application.dto.request.OAuth2LoginReqDTO;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.exception.InvalidAuthException;
import timetogeter.context.auth.application.validator.AuthValidator;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.application.dto.RegisterResponse;
import timetogeter.global.security.application.dto.TokenCommand;
import timetogeter.global.security.application.service.OAuth2UserDetailService;
import timetogeter.global.security.application.vo.principal.UserPrincipal;
import timetogeter.global.security.exception.AuthFailureException;
import timetogeter.global.security.infrastructure.oauth2.client.OAuth2Client;
import timetogeter.global.security.infrastructure.oauth2.client.OAuth2ClientProvider;
import timetogeter.global.security.util.jwt.JwtTokenProvider;
import timetogeter.global.security.util.redis.RedisUtil;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final RedisUtil redisUtil;

    private final OAuth2ClientProvider oAuth2ClientProvider;
    private final OAuth2UserDetailService oAuth2UserDetailService;
    private final LoginAttemptService loginAttemptService;

    @Transactional
    public void signUp(UserSignUpDTO dto) {
        User user = new User(dto, passwordEncoder);
        authValidator.validateDuplicateId(dto.getUserId());

        userRepository.save(user);
    }

    public String reissueToken(String refreshToken) {
        String userId = jwtTokenProvider.validateToken(refreshToken);
        authValidator.validateRefreshToken(userId, refreshToken);

        String accessToken = jwtTokenProvider.generateTokenFromRefreshToken(refreshToken);
        return accessToken;
    }

    // TODO: 로그인 예외처리 세분화
    public TokenCommand login(LoginReqDTO dto) {
        String userId = dto.getUserId();
        try {
            if(loginAttemptService.isLocked(userId)){
                throw new AuthFailureException(BaseErrorCode.ACCOUNT_LOCKED, "[ERROR] 계정이 잠겨 있습니다. 나중에 다시 시도하세요.");
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword()));
            // 성공 시 실패 카운트 초기화
            loginAttemptService.resetFailedAttempts(userId);

            TokenCommand token = jwtTokenProvider.generateToken(authentication);
            redisUtil.saveRefreshToken(dto.getUserId(), token.getRefreshToken());
            return token;
        }catch(AuthenticationException e){
            // 인증 실패 시 카운트 증가
            loginAttemptService.increaseFailedAttempts(userId);
            throw new AuthFailureException(BaseErrorCode.FAIL_LOGIN, "[ERROR] 아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    public TokenCommand login(OAuth2LoginReqDTO dto){
        try {
            Map<String, Object> attributes = getUserAttributes(dto);
            RegisterResponse registerResponse = oAuth2UserDetailService.loadOAuth2User(
                    Provider.valueOf(dto.getProvider()), attributes);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    new UserPrincipal(registerResponse), null,
                    List.of(new SimpleGrantedAuthority(Role.USER.name()))
            );

            TokenCommand token = jwtTokenProvider.generateToken(authentication);
            redisUtil.saveRefreshToken(registerResponse.email(), token.getRefreshToken());

            return token;
        }catch(Exception e){
            log.info(e.getMessage());
            throw new AuthFailureException(BaseErrorCode.FAIL_LOGIN, "[ERROR] 소셜로그인에 실패했습니다.");
        }
    }

    private Map<String, Object> getUserAttributes(OAuth2LoginReqDTO dto) {
        Provider provider = Provider.valueOf(dto.getProvider().toUpperCase());
        OAuth2Client client = oAuth2ClientProvider.getClient(provider);
        String accessToken = client.getAccessToken(dto.getCode(), dto.getRedirectUri());
        return client.getUserInfo(accessToken);
    }

    // TODO: 도중에 실패했을 경우 고려
    public void logout(String userId, String accessToken) {
        redisUtil.deleteRefreshToken(userId);

        long expiration = jwtTokenProvider.getExpiration(accessToken);
        if (expiration > 0) {
            redisUtil.setBlackList(accessToken, "logout", expiration);
        }
    }
}
