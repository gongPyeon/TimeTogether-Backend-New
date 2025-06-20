package timetogeter.context.promise.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.promise.application.dto.response.UserInfoResDTO;
import timetogeter.context.promise.application.service.PromiseSecurityService;
import timetogeter.context.promise.application.dto.response.UserIdsResDTO;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promise")
public class PromiseController {

    private final PromiseSecurityService promiseSecurityService;
    @GetMapping("/mem/s1/{promiseId}")
    public BaseResponse<Object> getUsersByPromiseTime(@PathVariable("promiseId") String promiseId) {
        UserIdsResDTO dto = promiseSecurityService.getUsersByPromiseTime(promiseId);
        return new BaseResponse<>(dto);
    }

    @GetMapping("/mem/s2/{promiseId}")
    public BaseResponse<Object> getUserNamesByDTO(@PathVariable("promiseId") String promiseId,
                                                  @RequestBody UserIdsResDTO reqDTO) {
        UserInfoResDTO dto = promiseSecurityService.getUserInfoByDTO(promiseId, reqDTO);
        return new BaseResponse<>(dto);
    }
}
