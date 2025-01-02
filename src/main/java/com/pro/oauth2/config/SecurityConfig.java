package com.pro.oauth2.config;

import com.pro.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.pro.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.pro.oauth2.jwt.JwtAuthenticationFilter;
import com.pro.oauth2.jwt.JwtService;
import com.pro.oauth2.repository.CookieAuthorizationRequestRepository;
import com.pro.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtService jwtService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

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
                        .requestMatchers("/oauth2/**", "/", "/login", "/logout").permitAll()
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
                        .successHandler(oAuth2AuthenticationSuccessHandler) // 성공 핸들러
                        .failureHandler(oAuth2AuthenticationFailureHandler)); // 실패 핸들러

        // 로그아웃
        http
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID") // jwt는 sessionles인데 어디서 설정되는거지?
                        .deleteCookies("accessToken")
                        .logoutSuccessUrl("/logout")); // 기존 login과 차이점이 있어야하는지?

        // jwt filter 설정
        http.addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 암호화 사용
    }
}
