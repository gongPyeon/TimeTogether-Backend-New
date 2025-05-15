package timetogeter.context.auth.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import timetogeter.context.auth.application.exception.InvalidAuthException;
import timetogeter.context.auth.domain.repository.UserRepository;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UserRepository userRepository;
    public String isDuplicateId(String userId){
        if(userRepository.existsById(userId))
            throw new InvalidAuthException(BaseErrorCode.INVALID_ID_DUP, "[ERROR] 중복된 아이디입니다.");
        return BaseCode.SUCCESS_ID.getMessage();
    }
}
