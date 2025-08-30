package timetogeter.context.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import timetogeter.context.auth.application.dto.request.LoginReqDTO;
import timetogeter.context.auth.application.dto.request.OAuth2LoginReqDTO;
import timetogeter.context.auth.application.dto.request.UserSignUpDTO;
import timetogeter.context.auth.application.dto.response.testDTO;
import timetogeter.context.auth.application.service.AuthService;
import timetogeter.global.interceptor.response.BaseCode;
import timetogeter.global.interceptor.response.BaseResponse;
import timetogeter.context.auth.application.dto.TokenCommand;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.global.interceptor.response.error.dto.ErrorResponse;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;
import timetogeter.global.security.util.cookie.CookieUtil;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "인증", description = "회원가입/로그인 관련 API")
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


    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입을 진행한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),

            @ApiResponse(responseCode = "400", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "중복 아이디", summary = "다른 아이디 사용",
                                            value = """
                                                    { "code": 400, "message": "다른 아이디를 사용해주세요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "아이디 길이 오류", summary = "아이디는 1자 이상 20자 이하",
                                            value = """
                                                    { "code": 400, "message": "아이디는 1자 이상 20자 이하로 입력해주세요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "아이디 형식 오류", summary = "아이디 형식 확인",
                                            value = """
                                                    { "code": 400, "message": "아이디 형식을 다시 확인해주세요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "닉네임 길이 오류", summary = "닉네임은 1자 이상 20자 이하",
                                            value = """
                                                    { "code": 400, "message": "닉네임은 1자 이상 20자 이하로 입력해주세요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "닉네임 형식 오류", summary = "닉네임 형식 확인",
                                            value = """
                                                    { "code": 400, "message": "닉네임 형식을 다시 확인해주세요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "이메일 형식 오류", summary = "이메일 형식 확인",
                                            value = """
                                                    { "code": 400, "message": "이메일 형식을 다시 확인해주세요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "전화번호 형식 오류", summary = "전화번호 형식 확인",
                                            value = """
                                                    { "code": 400, "message": "전화번호 형식을 다시 확인해주세요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/sign-up")
    public BaseResponse<String> signUp(@RequestBody @Valid UserSignUpDTO userSignUpDto) {
        authService.signUp(userSignUpDto);
        return new BaseResponse<>(BaseCode.SUCCESS_SIGN_UP);
    }


    @Operation(summary = "일반 로그인", description = "일반 로그인을 진행한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),

            @ApiResponse(responseCode = "401", description = "요청 형식 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "아이디 또는 비밀번호 형식 오류", summary = "아이디 또는 비밀번호를 확인",
                                            value = """
                                                    { "code": 401, "message": "아이디 또는 비밀번호를 확인해주세요" }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "계정 잠김", summary = "계정 해제 시간 확인",
                                            value = """
                                                    { "code": 401, "message": "계정이 잠겨 있습니다. 나중에 다시 시도하세요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/login")
    public BaseResponse<Object> login(@RequestBody @Valid LoginReqDTO dto, HttpServletResponse response) {
        TokenCommand token = authService.login(dto);

        response.setHeader(AUTHORIZATION, BEARER + token.accessToken());
        CookieUtil.addCookie(response, REFRESH_TOKEN, token.refreshToken(),
                Math.toIntExact(token.refreshTokenExpirationTime()));

        return new BaseResponse<>(BaseCode.SUCCESS_LOGIN);
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 진행한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "소셜 플랫폼 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "소셜 플랫폼 오류", summary = "소셜 플랫폼 확인",
                                            value = """
                                                    { "code": 400, "message": "지원하지 않는 소셜 플랫폼이에요" }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500", description = "소셜 로그인 토큰 발급 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "소셜 로그인 토큰 발급 오류", summary = "토큰 발급 확인",
                                            value = """
                                                    { "code": 500, "message": "소셜 로그인 토큰 발급을 실패했어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/oauth2/login")
    // KAKAO, NAVER, GOOGLE
    public BaseResponse<Object> login(@RequestBody @Valid OAuth2LoginReqDTO dto,
                                      HttpServletResponse response) {
        TokenCommand token = authService.login(dto);

        response.setHeader(AUTHORIZATION, BEARER + token.accessToken());
        CookieUtil.addCookie(response, REFRESH_TOKEN, token.refreshToken(),
                Math.toIntExact(token.refreshTokenExpirationTime()));

        return new BaseResponse<>(BaseCode.SUCCESS_LOGIN);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰을 재발급한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 유효성 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "토큰 유효성 오류", summary = "토큰 확인",
                                            value = """
                                                    { "code": 401, "message": "토큰이 유효하지 않아요" }
                                                    """
                                    )
                            }
                    )
            ),
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
    @PostMapping("/refresh")
    @SecurityRequirement(name = "BearerAuth")
    public BaseResponse<String> reissueToken(@RequestHeader("refresh-token") String refreshToken, HttpServletResponse response){
        String accessToken = authService.reissueToken(refreshToken);
        response.setHeader(AUTHORIZATION, BEARER + accessToken);

        return new BaseResponse<>(BaseCode.SUCCESS_REISSUE);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰을 재발급한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 유효성 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "토큰 유효성 오류", summary = "토큰 확인",
                                            value = """
                                                    { "code": 401, "message": "토큰이 유효하지 않아요" }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500", description = "세션 처리 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "세션 처리 오류", summary = "로그아웃 실패",
                                            value = """
                                                    { "code": 500, "message": "세션 처리에 실패했어요" }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/logout")
    @SecurityRequirement(name = "BearerAuth")
    public BaseResponse<String> logout(HttpServletRequest request, HttpServletResponse response,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestHeader("Authorization") String authHeader) {
        String userId = userPrincipal.getId();
        String accessToken = authHeader.replace(BEARER, "");

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        authService.logout(userId, accessToken);

        return new BaseResponse<>(BaseCode.SUCCESS_LOGOUT);
    }
}
