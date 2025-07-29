package timetogeter.global.security.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogeter.context.auth.exception.AuthExceptionHandler;
import timetogeter.context.auth.exception.AuthFailureException;
import timetogeter.context.auth.exception.InvalidJwtException;
import timetogeter.context.auth.exception.UserNotFoundException;
import timetogeter.global.interceptor.response.error.status.BaseErrorCode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthExceptionHandlerTest {
    @RestController
    @RequestMapping("/test")
    static class AuthExceptionTestController {

        @GetMapping("/inv-jwt")
        public void invJwt() { throw new InvalidJwtException(BaseErrorCode.INVALID_TOKEN, "InvJwt"); }

        @GetMapping("/auth-fail")
        public void authFail() { throw new AuthFailureException(BaseErrorCode.INVALID_AUTH, "AuthFail");}

        @GetMapping("/inv-user")
        public void invUser() { throw new UserNotFoundException(BaseErrorCode.INVALID_USER, "InvUser");}
    }

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthExceptionTestController())
                .setControllerAdvice(new AuthExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("AuthException 예외가 발생할 시")
    class AuthExceptionTest{

        @Test
        @DisplayName("성공: InvalidJwtException 잡고 처리")
        void throwInvalidJwtEx() throws Exception{
            // given & when
            ResultActions resultActions = mockMvc.perform(get("/test/inv-jwt"));

            // then
            resultActions.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").isString());
        }

        @Test
        @DisplayName("성공: AuthFailureException 잡고 처리")
        void throwAuthFailureExOne() throws Exception{
            // given & when
            ResultActions resultActions = mockMvc.perform(get("/test/auth-fail"));

            // then
            resultActions.andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$.message").isString());
        }

        @Test
        @DisplayName("성공: UserNotFoundException 잡고 처리")
        void throwUserNotFound() throws Exception {
            // given & when
            ResultActions resultActions = mockMvc.perform(get("/test/inv-user"));

            // then
            resultActions.andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").isString());
        }

    }

}