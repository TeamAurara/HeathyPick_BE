package com.soongsil.eolala.auth.application;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CookieService {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    
    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    /**
     * Refresh Token을 HttpOnly 쿠키로 설정
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, Long expiresInMs) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
            .httpOnly(true)                    // XSS 공격 방어
            .secure(isProduction())            // HTTPS 환경에서만 전송 (운영환경)
            .path("/")                         // 모든 경로에서 전송
            .maxAge(expiresInMs / 1000)       // 만료 시간 (밀리초 → 초)
            .sameSite("Strict")               // CSRF 공격 방어
            .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.debug("Refresh Token 쿠키 설정 완료 - 만료시간: {}초", expiresInMs / 1000);
    }

    /**
     * Refresh Token 쿠키 삭제
     */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
            .httpOnly(true)
            .secure(isProduction())
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.debug("Refresh Token 쿠키 삭제 완료");
    }

    /**
     * 운영 환경 여부 확인
     */
    private boolean isProduction() {
        return "prod".equals(activeProfile);
    }
} 