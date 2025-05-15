package timetogeter.global.security.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.global.security.application.dto.RegisterResponse;
import timetogeter.global.security.application.dto.RegisterUserCommand;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;

    // 소셜로그인일 경우
    @Transactional
    public RegisterResponse getOrRegisterUser(RegisterUserCommand registerUserCommand) {
        User findUser = userRepository.findByProviderIdAndProviderType(
                        registerUserCommand.userId(),
                        registerUserCommand.provider())
                .orElseGet(()->{
                    User user = User.create(registerUserCommand);
                    userRepository.save(user);
                    return user;
                });

        return RegisterResponse.from(findUser);
    }

    // 일반 로그인일 경우
    public RegisterResponse getRegisterUser(String userId) {
        User findUser = userRepository.findByUserId(userId);
        // TODO: 예외처리 아이디 또는 비밀번호를 확인해주세요. (사용자를 찾을 수 없어요)

        return RegisterResponse.from(findUser);
    }
}
