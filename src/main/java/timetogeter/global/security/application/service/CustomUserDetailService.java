package timetogeter.global.security.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import timetogeter.global.security.application.dto.RegisterResponse;

import java.nio.file.attribute.UserPrincipal;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRegisterService userRegisterService;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        RegisterResponse response = userRegisterService.getRegisterUser(userId);
        return new UserPrincipal(response);
    }
}
