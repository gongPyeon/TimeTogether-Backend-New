package timetogeter.context.auth.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogeter.context.auth.domain.vo.Gender;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;

@Entity
@Getter
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
}
