package timetogeter.context.auth.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.exception.InvalidAuthException;
import timetogeter.context.auth.application.validator.AuthValidator;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.util.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final JwtTokenProvider jwtTokenProvider;

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
}
