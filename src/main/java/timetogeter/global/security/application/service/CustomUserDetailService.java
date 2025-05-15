package timetogeter.global.security.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.context.promise.domain.entity.Promise;
import timetogeter.global.security.application.dto.RegisterResponse;
import timetogeter.global.security.application.dto.RegisterUserCommand;
import timetogeter.global.security.application.vo.principal.UserPrincipal;
import timetogeter.global.security.exception.UserNotFoundException;
import timetogeter.global.security.infrastructure.oauth2.userInfo.OAuth2UserInfo;
import timetogeter.global.security.infrastructure.oauth2.userInfo.OAuth2UserInfoFactory;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRegisterService userRegisterService;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UserNotFoundException {
        RegisterResponse response = userRegisterService.getRegisterUser(userId);
        return new UserPrincipal(response);
    }
}
