package timetogeter.context.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.dto.response.testDTO;
import timetogeter.context.auth.application.service.AuthService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    // TODO: 삭제 예정
    @GetMapping(value = "/restDocsTest", produces = MediaType.APPLICATION_JSON_VALUE)
    public testDTO restDocsTestAPI() {
        return new testDTO("test!!");
    }

    @GetMapping(value = "/restDocsTest/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public testDTO restDocsTestParameterAPI(@PathVariable Long id) {
        return new testDTO(id + "test!!");
    }


    private final AuthService authService;
    @PostMapping("/sign-up")
    public BaseResponse<String> signUp(@Valid @RequestBody UserSignUpDTO userSignUpDto){
        // TODO: DTO 내부에서 검증처리를 하는 경우, 예외메세지를 분명하게 받을 수 없으므로 NULL 만 적용
        String message = authService.signUp(userSignUpDto);
        return new BaseResponse<>(message);
    }

    @PostMapping("/refresh")
    public BaseResponse<String> reissueToken(@RequestHeader("refresh-token") String refreshToken, HttpServletResponse response){
        String accessToken = authService.reissueToken(refreshToken);
        response.setHeader("Authorization", "Bearer " + accessToken);

        return new BaseResponse<>(BaseCode.SUCCESS_REISSUE);
    }
}
