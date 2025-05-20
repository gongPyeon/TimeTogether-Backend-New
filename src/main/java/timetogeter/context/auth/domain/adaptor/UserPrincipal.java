package timetogeter.context.auth.domain.adaptor;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.context.auth.application.dto.RegisterResponse;
import timetogeter.context.auth.application.exception.AuthFailureException;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {

    private final RegisterResponse registerResponse;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    // 소셜 로그인
    public UserPrincipal(RegisterResponse registerResponse, Map<String, Object> attributes) {
        if (registerResponse == null) throw new AuthFailureException(BaseErrorCode.INVALID_AUTH, "[ERROR] RegisterResponse가 null이어서 UserPrincipal을 생성하지 못했습니다.");
        this.registerResponse = registerResponse;
        this.attributes = attributes;
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(registerResponse.role().name())
        );
    }

    // 일반 로그인
    public UserPrincipal(RegisterResponse registerResponse) {
        if (registerResponse == null) throw new AuthFailureException(BaseErrorCode.INVALID_AUTH, "[ERROR] RegisterResponse가 null이어서 UserPrincipal을 생성하지 못했습니다.");
        this.registerResponse = registerResponse;
        this.attributes = null;
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(registerResponse.role().name())
        );
    }


    // TODO: 사용할 때 null인지 확인
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getId(){
        return registerResponse.userId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return registerResponse.password();
    }

    @Override
    public String getUsername() {
        return registerResponse.nickname();
    }

    @Override
    public String getName() {
        return registerResponse.nickname();
    }

    public Provider getProvider() { return registerResponse.provider(); }
    public Role getRole() {
        return registerResponse.role();
    }

}
