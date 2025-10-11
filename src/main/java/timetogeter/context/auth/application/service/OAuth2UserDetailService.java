package timetogeter.context.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.context.auth.application.dto.RegisterResponse;
import timetogeter.context.auth.application.dto.RegisterUserCommand;
import timetogeter.context.auth.infrastructure.external.oauth2.userInfo.OAuth2UserInfo;
import timetogeter.context.auth.infrastructure.external.oauth2.userInfo.OAuth2UserInfoFactory;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserDetailService {
    private final UserRegisterService userRegisterService;
    public RegisterResponse loadOAuth2User(Provider providerType, Map<String, Object> attributes) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, attributes);

        RegisterUserCommand registerUserCommand = new RegisterUserCommand(
                oAuth2UserInfo.getOAuth2Id(),
                oAuth2UserInfo.getEmail(),
                null, // telephone
                oAuth2UserInfo.getName(),
                providerType,
                Role.USER,
                null, // age
                null, // gender
                null,
                null,
                null
        );

        return userRegisterService.getOrRegisterUser(registerUserCommand);
    }
}
