package timetogeter.context.auth.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.application.dto.request.UserIdDTO;
import timetogeter.context.auth.application.validator.AuthValidator;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/check")
public class AuthCheckController {

    private final AuthValidator authValidator;
    @PostMapping("/id")
    public BaseResponse<String> checkDuplicateId(@RequestBody UserIdDTO dto){
        String message = authValidator.isDuplicateId(dto.getUserId());
        return new BaseResponse<>(message);
    }
}
