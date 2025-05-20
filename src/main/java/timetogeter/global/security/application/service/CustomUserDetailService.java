package timetogeter.global.security.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.application.dto.RegisterResponse;
import timetogeter.context.auth.application.service.UserRegisterService;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.auth.application.exception.UserNotFoundException;

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
