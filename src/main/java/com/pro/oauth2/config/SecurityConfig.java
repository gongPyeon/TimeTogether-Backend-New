package com.pro.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.oauth2.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.pro.oauth2.handler.AuthenticationFailureHandler;
import com.pro.oauth2.handler.AuthenticationSuccessHandler;
import com.pro.oauth2.jwt.JwtAuthenticationFilter;
import com.pro.oauth2.jwt.JwtService;
import com.pro.oauth2.repository.CookieAuthorizationRequestRepository;
import com.pro.oauth2.repository.RefreshTokenRepository;
import com.pro.oauth2.service.CustomOAuth2UserService;
import com.pro.oauth2.service.LoginService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtService jwtService;
    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // sessionless이므로 httpBasic, csrf, formLogin, session disable
        http
                .cors(cors -> cors.configurationSource(corsConfig.configurationSource()))
                .csrf(csrf ->csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .rememberMe(rememberMe -> rememberMe.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 요청에 대한 권한 설정
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**", "/", "/login", "/login-test", "/sign-up").permitAll()
                        .anyRequest().authenticated());

        // oauth2 로그인
        http
                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(endpoint -> endpoint
//                                .baseUri("/oauth2/authorize")
//                                .authorizationRequestRepository(cookieAuthorizationRequestRepository) // 수정된 부분
//                        )
//                        .redirectionEndpoint(redirect -> redirect.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(info -> info.userService(customOAuth2UserService)) // 회원 정보 처리 (Oauth2User를 반환)
                        .successHandler(authenticationSuccessHandler) // 성공 핸들러
                        .failureHandler(authenticationFailureHandler)); // 실패 핸들러

        // 로그아웃
        http
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID") // jwt는 sessionles인데 어디서 설정되는거지?
                        .deleteCookies("accessToken")
                        .logoutSuccessUrl("/logout-test")); // 기존 login과 차이점이 있어야하는지?

        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 암호화 사용
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return customJsonUsernamePasswordLoginFilter;
    }

    // jwt filter 설정
    @Bean
    public Filter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, refreshTokenRepository);
        return jwtAuthenticationFilter;
    }

}
