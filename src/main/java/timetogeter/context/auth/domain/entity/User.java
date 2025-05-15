package timetogeter.context.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import timetogeter.context.auth.domain.vo.Gender;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.global.security.application.dto.RegisterUserCommand;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private String userId;
    private String userImg;
    private String email;

    // @Getter(AccessLevel.NONE) > 회원가입 시 필요
    private String password;
    private String telephone;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String age;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public static User create(RegisterUserCommand registerUserCommand){
        // 검증
        return User.builder()
                .userId(registerUserCommand.userId())
                .email(registerUserCommand.email())
                .nickname(registerUserCommand.nickname())
                .userImg(null)
                .password(null)
                .telephone(registerUserCommand.telephone())
                .provider(registerUserCommand.provider())
                .role(registerUserCommand.role())
                .age(registerUserCommand.age())
                .gender(registerUserCommand.gender())
                .build();
    }
}
