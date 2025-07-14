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

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, Long expiresInMs) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
            .httpOnly(true)
            .secure(isProduction())
            .path("/")
            .maxAge(expiresInMs / 1000)
            .sameSite("Strict")
            .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.debug("Refresh Token 쿠키 설정 완료 - 만료시간: {}초", expiresInMs / 1000);
    }

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

    private boolean isProduction() {
        return "prod".equals(activeProfile);
    }
} 