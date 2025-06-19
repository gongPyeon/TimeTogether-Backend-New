package timetogeter.context.auth.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.context.auth.application.dto.RegisterResponse;
import timetogeter.context.auth.application.dto.RegisterUserCommand;
import timetogeter.context.auth.exception.UserNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;

    // 소셜로그인일 경우
    @Transactional
    public RegisterResponse getOrRegisterUser(RegisterUserCommand registerUserCommand) {
        User findUser = userRepository.findByIdAndProviderType(
                        registerUserCommand.userId(),
                        registerUserCommand.provider())
                .orElseGet(()->{
                    User user = new User(registerUserCommand);
                    userRepository.save(user);
                    return user;
                });

        return RegisterResponse.from(findUser);
    }

    // 일반 로그인일 경우
    public RegisterResponse getRegisterUser(String userId) {
        User findUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(BaseErrorCode.INVALID_LOGIN, "[ERROR]: "+userId+"에 해당하는 사용자를 찾을 수 없습니다"));

        return RegisterResponse.from(findUser);
    }
}
