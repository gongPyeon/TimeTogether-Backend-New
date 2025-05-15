package timetogeter.context.group.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.group.application.dto.response.testDTO;
import timetogeter.global.interceptor.response.error.dto.SuccessResponseDto;

@RestController
public class NewGroupController {

    @GetMapping(value = "/newGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponseDto<testDTO> restDocsTestAPI() {
        throw new IllegalArgumentException("테스트용 예외 발생!");
        //return SuccessResponseDto.from(new testDTO("예외 테스트입니다."));
    }


}