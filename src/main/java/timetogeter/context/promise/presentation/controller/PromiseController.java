package timetogeter.context.promise.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.promise.application.dto.request.ExitPromiseReqDTO;
import timetogeter.context.promise.application.dto.response.PromiseSharedKeyResDTO;
import timetogeter.context.promise.application.dto.response.UserInfoResDTO;
import timetogeter.context.promise.application.service.PromiseSecurityService;
import timetogeter.context.promise.application.dto.response.UserIdsResDTO;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promise")
public class PromiseController {

    private final PromiseSecurityService promiseSecurityService;
    @GetMapping("/mem/s1/{promiseId}")
    public BaseResponse<Object> getUsersByPromiseTime1(@PathVariable("promiseId") String promiseId) {
        UserIdsResDTO dto = promiseSecurityService.getUsersByPromiseTime(promiseId);
        return new BaseResponse<>(dto);
    }

    @GetMapping("/mem/s2/{promiseId}")
    public BaseResponse<Object> getUsersByPromiseTime2(@PathVariable("promiseId") String promiseId,
                                                       @RequestBody UserIdsResDTO reqDTO) {
        UserInfoResDTO dto = promiseSecurityService.getUserInfoByDTO(promiseId, reqDTO);
        return new BaseResponse<>(dto);
    }

    @GetMapping("/exit")
    public BaseResponse<Object> exitPromise(@RequestBody ExitPromiseReqDTO reqDTO) {
        promiseSecurityService.exitPromise(reqDTO);
        return new BaseResponse<>(BaseCode.SUCCESS_EXIT_PROMISE);
    }
}
