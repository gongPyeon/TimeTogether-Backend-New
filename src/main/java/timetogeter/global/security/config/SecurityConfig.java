package timetogeter.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import timetogeter.global.security.exception.filter.ExceptionHandlerFilter;
import timetogeter.global.security.util.jwt.JwtAuthenticationProvider;
import timetogeter.global.security.util.jwt.TokenValidator;
import timetogeter.global.security.util.jwt.filter.JwtAuthenticationFilter;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CorsConfig corsConfig,
                                           JwtAuthenticationProvider jwtAuthenticationProvider,
                                           ExceptionHandlerFilter jwtExceptionHandlerFilter,
                                           TokenValidator tokenValidator) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .rememberMe(rememberMe -> rememberMe.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(requestPermitAll()).permitAll()
                        .anyRequest().authenticated());

        // TODO: filter 순서 설정
        // 예외 핸들러 > 토큰 인증 수행 및 보안 컨텍스트 설정(Jwt) > 로그아웃
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider, tokenValidator), LogoutFilter.class)
                .addFilterBefore(jwtExceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    private RequestMatcher[] requestPermitAll() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(GET, "/docs/**"),
                antMatcher(POST, "/static/docs/**"), // API 문서
                antMatcher(POST, "/auth/**"), // 회원가입 & 로그인
                antMatcher(GET, "/test/**")); // 테스트 시 (선택)


        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
