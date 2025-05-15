package timetogeter.context.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.exception.InvalidAuthException;
import timetogeter.context.auth.domain.vo.Gender;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
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

    public User(UserSignUpDTO dto) {
        validateUserId(dto.getUserId());
        User.builder()
                .userId(dto.getUserId())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .userImg(null)
                .password(dto.getPassword())
                .telephone(dto.getTelephone()) // 선택
                .provider(Provider.GENERAL)
                .role(Role.USER)
                .age(dto.getAge()) // 선택
                .gender(dto.getGender()) // 선택
                .build();
    }

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

    private void validateUserId(String userId) {
        if (userId == null || userId.length() < 1 || userId.length() > 20) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_ID_LENGTH,"[ERROR] 아이디는 1~20자여야 합니다.");
        }
        if (!userId.matches("^[a-zA-Z0-9_]+$")) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_ID_FORMAT,"[ERROR] 아이디는 영어, 숫자, 언더바만 가능합니다.");
        }
    }
}
