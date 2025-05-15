package timetogeter.context.auth.application.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import timetogeter.context.auth.domain.vo.Gender;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;

@Getter
public class UserSignUpDTO { // TODO: 언제 class를 쓰고 언제 record를 쓰는지 확인하기
    @NotNull
    private String userId;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String rePassword;
    @NotNull
    private String nickname;

    private String telephone;
    private String age;
    private Gender gender;

}
