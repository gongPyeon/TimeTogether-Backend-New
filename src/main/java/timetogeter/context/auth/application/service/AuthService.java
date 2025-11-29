package timetogeter.context.auth.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.dto.request.LoginReqDTO;
import timetogeter.context.auth.application.dto.request.OAuth2LoginDetailReqDTO;
import timetogeter.context.auth.application.dto.request.OAuth2LoginReqDTO;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.dto.response.LoginResDTO;
import timetogeter.context.auth.application.validator.AuthValidator;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.context.auth.application.dto.RegisterResponse;
import timetogeter.context.auth.application.dto.TokenCommand;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.auth.exception.AuthFailureException;
import timetogeter.context.auth.infrastructure.external.oauth2.client.OAuth2Client;
import timetogeter.context.auth.infrastructure.external.oauth2.client.OAuth2ClientProvider;
import timetogeter.global.security.util.jwt.JwtTokenProvider;
import timetogeter.global.common.util.redis.RedisUtil;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static timetogeter.global.security.util.DataUtil.*;


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
        authValidator.validateDuplicateId(dto.userId());

        userRepository.save(user);
    }

    public String reissueToken(String refreshToken) {
        String userId = jwtTokenProvider.validateToken(refreshToken);
        authValidator.validateRefreshToken(userId, refreshToken);

        String accessToken = jwtTokenProvider.generateTokenFromRefreshToken(refreshToken);
        return accessToken;
    }

    public LoginResDTO login(LoginReqDTO dto) {
        String userId = dto.userId();
        try {
            if(loginAttemptService.isLocked(userId)){
                throw new AuthFailureException(BaseErrorCode.ACCOUNT_LOCKED, "[ERROR] 계정이 잠겨 있습니다. 나중에 다시 시도하세요.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, dto.password()));
            loginAttemptService.resetFailedAttempts(userId); // 성공 시 실패 카운트 초기화
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            TokenCommand token = jwtTokenProvider.generateToken(authentication);
            redisUtil.set(REFRESH_HEADER + userId, token.refreshToken(), token.refreshTokenExpirationTime(), TimeUnit.SECONDS);
            return new LoginResDTO(token, userPrincipal.getImgIv(), userPrincipal.getEmailIv(), userPrincipal.getPhoneIv());
        }catch(AuthenticationException e){
            // 인증 실패 시 카운트 증가
            loginAttemptService.increaseFailedAttempts(userId);
            throw new AuthFailureException(BaseErrorCode.FAIL_LOGIN, "[ERROR] 아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    public LoginResDTO login(OAuth2LoginReqDTO dto){
        try {
            Map<String, Object> attributes = getUserAttributes(dto);
            RegisterResponse registerResponse = oAuth2UserDetailService.loadOAuth2User(
                    Provider.valueOf(dto.provider()), attributes);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    new UserPrincipal(registerResponse, attributes), null,
                    List.of(new SimpleGrantedAuthority(Role.USER.name()))
            );

            TokenCommand token = jwtTokenProvider.generateToken(authentication);
            redisUtil.set(REFRESH_HEADER + registerResponse.userId(), token.refreshToken(), token.refreshTokenExpirationTime(), TimeUnit.SECONDS);

            return new LoginResDTO(token, registerResponse.imgIv(), registerResponse.emailIv(), registerResponse.phoneIv());
        }catch (RedisConnectionFailureException e) {
            log.info(e.getMessage());
            throw new AuthFailureException(BaseErrorCode.REDIS_ERROR, "[ERROR] 세션 저장에 실패했습니다.");
        }catch(Exception e){
            log.info(e.getMessage());
            throw new AuthFailureException(BaseErrorCode.FAIL_LOGIN, "[ERROR] 소셜로그인에 실패했습니다.");
        }
    }

    private Map<String, Object> getUserAttributes(OAuth2LoginReqDTO dto) {
        Provider provider = Provider.valueOf(dto.provider().toUpperCase());
        OAuth2Client client = oAuth2ClientProvider.getClient(provider);
        String accessToken = client.getAccessToken(dto.code(), dto.redirectUri());
        return client.getUserInfo(accessToken);
    }

    public void logout(String userId, String accessToken) {
        try {
            redisUtil.delete(REFRESH_HEADER + userId);
            long expiration = jwtTokenProvider.getExpiration(accessToken);
            if (expiration > 0) {
                redisUtil.set(BL_HEADER + accessToken, LOGOUT, Duration.ofSeconds(expiration));
            }
        } catch (RedisConnectionFailureException e) {
            log.error("{}:{}", userId, e.getMessage());
            throw new AuthFailureException(BaseErrorCode.REDIS_ERROR, "[ERROR] 로그아웃 중 토큰 삭제에 실패했습니다.");
        }
    }

    @Transactional
    public void setDetail(OAuth2LoginDetailReqDTO dto) {
        User user = userRepository.findByUserId(dto.userId())
                .orElseThrow(() -> new AuthFailureException(BaseErrorCode.INVALID_USER, "[ERROR] 소셜 회원가입이 완료되지 않은 상태입니다."));

        user.setDetail(dto);
        userRepository.save(user);
    }
}
