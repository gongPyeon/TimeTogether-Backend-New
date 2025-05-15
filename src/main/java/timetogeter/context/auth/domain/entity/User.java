package timetogeter.context.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public User(UserSignUpDTO dto, PasswordEncoder passwordEncoder) {
        validate(dto.getUserId(), dto.getNickname(), dto.getEmail(), dto.getTelephone());
        this.userId = dto.getUserId();
        this.email = dto.getEmail();
        this.nickname = dto.getNickname();
        this.password = passwordEncoder.encode(dto.getPassword());
        this.telephone = dto.getTelephone();
        this.provider = Provider.GENERAL;
        this.role = Role.USER;
        this.age = dto.getAge();
        this.gender = dto.getGender();
    }

    public User(RegisterUserCommand dto){
        // 검증
        validate(dto.userId(), dto.nickname(), dto.email(), dto.telephone());
        this.userId = dto.userId();
        this.email = dto.email();
        this.nickname = dto.nickname();
        this.password = null;
        this.telephone = dto.telephone();
        this.provider = dto.provider();
        this.role = dto.role();
        this.age = dto.age();
        this.gender = dto.gender();
    }

    private void validate(String userId, String nickname, String email, String telephone){
        validateUserId(userId);
        validateUserNickname(nickname);
        validateEmail(email);
        validatePhone(telephone);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.length() < 1 || userId.length() > 20) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_ID_LENGTH,"[ERROR] 아이디는 1~20자여야 합니다.");
        }
        if (!userId.matches("^[a-zA-Z0-9_]+$")) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_ID_FORMAT,"[ERROR] 아이디는 영어, 숫자, 언더바만 가능합니다.");
        }
    }

    private void validateUserNickname(String nickname) {
        if (nickname == null || nickname.length() < 1 || nickname.length() > 20) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_NICKNAME_LENGTH,"[ERROR] 닉네임은 1~20자여야 합니다.");
        }
        if (!nickname.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_NICKNAME_FORMAT,"[ERROR] 닉네임은 영어, 숫자, 한글만 가능합니다.");
        }
    }

    private void validateEmail(String email){
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_EMAIL_FORMAT, "[ERROR] 이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validatePhone(String phone){
        if(phone == null) return;
        if (!phone.matches("^01[016789]-\\d{3,4}-\\d{4}$")) {
            throw new InvalidAuthException(BaseErrorCode.INVALID_PHONE_FORMAT, "[ERROR] 전화번호 형식이 올바르지 않습니다.");
        }
    }
}
