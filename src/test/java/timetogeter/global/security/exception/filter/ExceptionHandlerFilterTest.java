package timetogeter.global.security.exception.filter;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerFilterTest {

    @InjectMocks
    private ExceptionHandlerFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUP(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("filterChain에서 오류가 발생할 경우")
    class filterChainTest{
        @Test
        @DisplayName("성공: Jwt 예외를 잡는다")
        void throwInvalidJwt() throws ServletException, IOException {
            FilterChain mockChain = (req, res) -> {
                throw new JwtException("JWT 오류");
            };

            filter.doFilterInternal(request, response, mockChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }

        @Test
        @DisplayName("성공: Authentication 예외를 잡는다")
        void throwAuthentication() throws ServletException, IOException {
            FilterChain mockChain = (req, res) -> {
                throw new AuthenticationException("인증 실패") {};
            };

            filter.doFilterInternal(request, response, mockChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }

        @Test
        @DisplayName("성공: AccessDenied 예외를 잡는다")
        void throwAccessDenied() throws ServletException, IOException {
            FilterChain mockChain = (req, res) -> {
                throw new AccessDeniedException("접근 거부");
            };

            filter.doFilterInternal(request, response, mockChain);

            assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        }

        @Test
        @DisplayName("성공: RuntimeException 예외를 잡는다")
        void throwRuntime() throws ServletException, IOException {
            FilterChain mockChain = (req, res) -> {
                throw new RuntimeException("서버 내부 오류");
            };

            filter.doFilterInternal(request, response, mockChain);

            assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        }

    }

}