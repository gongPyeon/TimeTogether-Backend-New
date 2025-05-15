package timetogeter.context.auth.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.exception.InvalidAuthException;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public String signUp(UserSignUpDTO dto) {
        User user = new User(dto);
        duplicate(dto.getUserId(), dto.getNickname());

        userRepository.save(user);
        return BaseCode.SUCCESS_SIGN_UP.getMessage();
    }

    private void duplicate(String userId, String nickname) {
        if(userRepository.existsById(userId))
            throw new InvalidAuthException(BaseErrorCode.INVALID_ID_DUP, "[ERROR] 중복된 아이디입니다.");
        if(userRepository.existsByNickname(nickname))
            throw new InvalidAuthException(BaseErrorCode.INVALID_NICKNAME_DUP, "[ERROR] 중복된 닉네임입니다.");
    }
}
