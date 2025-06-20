package timetogeter.context.time.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.place.application.dto.response.PlaceBoardDTO;
import timetogeter.context.time.application.dto.response.TimeBoardDTO;
import timetogeter.context.time.application.service.TimeBoardService;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/time")
public class TimeController {

    private final TimeBoardService timeBoardService;

    @GetMapping("/{promiseId}") // TODO: 엔티티 네이밍
    public BaseResponse<Object> viewTimeBoard(@PathVariable("promiseId") String promiseId) {
        TimeBoardDTO dto = timeBoardService.getTimeBoard(promiseId);
        return new BaseResponse<>(dto);
    }
}
