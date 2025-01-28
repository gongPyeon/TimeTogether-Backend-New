package com.pro.oauth2.entity;

import com.pro.domain.group_user_middle.domain.GroupUserMiddle;
import com.pro.domain.schedule.domain.Schedule;
import com.pro.oauth2.enums.Role;
import com.pro.oauth2.enums.SocialType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import com.pro.oauth2.userInfo.OAuth2UserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_mn_user")
public class User{

    //자체 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$", message = "이메일 형식에 어긋납니다.")
    @NotNull(message = "이메일은 필수값입니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotNull(message = "이름은 필수값입니다.")
    private String name;

    @NotNull
    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = true)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Nullable
    private String img;

    //연관관계 필드
    @OneToMany(mappedBy = "user")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<GroupUserMiddle> groupUserMiddles = new ArrayList<>();

    //Builder, of
    @Builder
    private User(String email, String password, String name, String oauth2Id, SocialType socialType, Role role, @Nullable String img) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.oauth2Id = oauth2Id;
        this.socialType = socialType;
        this.role = role;
        this.img = img;
    }

    public static User of(String email, String password, String name, String oauth2Id, SocialType socialType, Role role, @Nullable String img){
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .oauth2Id(oauth2Id)
                .socialType(socialType)
                .role(role)
                .img(img)
                .build();

    }

    public User update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();

        return this;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

}
