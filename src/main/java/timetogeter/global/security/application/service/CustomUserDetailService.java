package timetogeter.global.security.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import timetogeter.global.security.application.dto.RegisterResponse;
import timetogeter.global.security.application.vo.principal.UserPrincipal;
import timetogeter.global.security.exception.UserNotFoundException;

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
