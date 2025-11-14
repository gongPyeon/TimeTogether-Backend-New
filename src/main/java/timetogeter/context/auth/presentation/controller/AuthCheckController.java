package timetogeter.context.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.application.dto.request.UserIdDTO;
import timetogeter.context.auth.application.service.AuthService;
import timetogeter.context.auth.application.validator.AuthValidator;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/check")
public class AuthCheckController {

    private final AuthValidator authValidator;
    @PostMapping("/id")
    public BaseResponse<String> checkDuplicateId(@RequestBody UserIdDTO dto){
        authValidator.validateDuplicateId(dto.userId());
        return new BaseResponse<>(BaseCode.SUCCESS_ID);
    }

    @Operation(summary = "액세스 토큰 재발급 가능 확인", description = "액세스 토큰을 재발급을 위해 아이디를 검증한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "토큰 유효성 오류", summary = "리프레시 토큰 유효성 확인",
                                            value = """
                                                    { "code": 400, "message": "아이디에 해당하는 리프레시 토큰이 존재하지 않아요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "토큰 불일치 오류", summary = "리프레시 토큰 형식 확인",
                                            value = """
                                                    { "code": 400, "message": "아이디에 해당하는 리프레시 토큰과 일치하지 않아요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @GetMapping("/refresh")
    @SecurityRequirement(name = "BearerAuth")
    public BaseResponse<String> reissueToken(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestHeader("refresh-token") String refreshToken){
        String userId = userPrincipal.getId();
        authValidator.validateRefreshToken(userId, refreshToken);

        return new BaseResponse<>(BaseCode.SUCCESS_REFRESH_BY_ID);
    }
}
