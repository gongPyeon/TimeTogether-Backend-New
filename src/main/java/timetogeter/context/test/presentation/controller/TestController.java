package timetogeter.context.test.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/status")
public class TestController {
    @PostMapping("/health")
    public BaseResponse<String> getPromiseView(@AuthenticationPrincipal UserPrincipal userPrincipal){
        String userId = userPrincipal.getId();
        System.out.println(userId);
        return new BaseResponse<>("good health server!");
    }
}
