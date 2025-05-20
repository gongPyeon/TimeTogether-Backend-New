package timetogeter.global.security.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import timetogeter.context.auth.application.service.UserRegisterService;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.application.dto.RegisterResponse;
import timetogeter.context.auth.application.dto.RegisterUserCommand;
import timetogeter.context.auth.application.exception.UserNotFoundException;
import timetogeter.global.support.UserFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRegisterServiceTest {

    @InjectMocks
    private UserRegisterService userRegisterService;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("getOrRegisterUser 실행 시")
    class getOrRegisterUserTest{

        @Test
        @DisplayName("성공: 사용자가 존재할 경우")
        void successUserFound(){
            // given
            String userId = "test";
            Provider provider = Provider.GOOGLE;

            RegisterUserCommand registerUserCommand = UserFixture.registerUserCommand(userId);
            User user = UserFixture.userByRegisterCommand(registerUserCommand);

            when(userRepository.findByIdAndProviderType(userId, provider)).thenReturn(Optional.of(user));

            // when
            RegisterResponse result = userRegisterService.getOrRegisterUser(registerUserCommand);

            // then
            verify(userRepository).findByIdAndProviderType(userId, provider);
            assertThat(result.userId()).isEqualTo(userId);
        }

        @Test
        @DisplayName("성공: 사용자가 존재하지 않을 경우")
        void successUserNotFound(){
            // given
            String userId = "none";
            Provider provider = Provider.GOOGLE;

            RegisterUserCommand registerUserCommand = UserFixture.registerUserCommand(userId);
            User user = UserFixture.userByRegisterCommand(registerUserCommand);

            // 오류: Optional.empty()를 반환해야 정상적으로 orElseGet() 분기가 실행됨
            when(userRepository.findByIdAndProviderType(userId, provider)).thenReturn(Optional.empty());

            // when
            RegisterResponse result = userRegisterService.getOrRegisterUser(registerUserCommand);

            // then
            verify(userRepository).findByIdAndProviderType(userId, provider);
            verify(userRepository).save(any(User.class)); // TODO: 실제 user 객체로 할 경우 안되는 이유 확인하기
            assertThat(result.provider()).isEqualTo(provider);
        }
    }

    @Nested
    @DisplayName("getRegisterUser 실행 시")
    class getRegisterUserTest{

        @Test
        @DisplayName("성공")
        void success(){
            // given
            String userId = "test";
            User user = UserFixture.userById(userId);

            when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

            // when
            RegisterResponse result = userRegisterService.getRegisterUser(userId);

            // then
            verify(userRepository).findByUserId(userId);
            assertThat(result.userId()).isEqualTo(userId);
        }

        @Test
        @DisplayName("예외: 사용자가 등록되지 않은 경우")
        void throwUserNotFound(){
            // given
            String userId = "none";
            User user = UserFixture.userById(userId);

            when(userRepository.findByUserId(userId)).thenThrow(UserNotFoundException.class);

            // when & then
            assertThrows(UserNotFoundException.class,
                    () -> userRegisterService.getRegisterUser(userId));
        }
    }

}