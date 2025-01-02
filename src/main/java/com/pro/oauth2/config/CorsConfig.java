package com.pro.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    public final static String CLIENT_URL = "http://172.20.10.2:3000";
    @Bean
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(CLIENT_URL);
        corsConfiguration.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        corsConfiguration.addAllowedHeader("*"); // 모든 헤더 허용

        corsConfiguration.addExposedHeader("Authorization"); // 클라이언트가 Authorization 헤더를 읽을 수 있도록 허용
        corsConfiguration.setAllowCredentials(true); // 쿠키와 인증헤더 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
