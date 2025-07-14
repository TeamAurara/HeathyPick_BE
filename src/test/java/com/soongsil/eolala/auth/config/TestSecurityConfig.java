package com.soongsil.eolala.auth.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 테스트용 Security 설정 클래스
 * - 모든 요청을 허용하여 Controller 테스트에서 Security 복잡성을 제거
 * - 단위 테스트 순수성 보장
 * - 여러 테스트에서 재사용 가능
 */
@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }
} 