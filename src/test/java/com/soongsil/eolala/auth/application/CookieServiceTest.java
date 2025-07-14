package com.soongsil.eolala.auth.application;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("CookieService 단위 테스트")
class CookieServiceTest {

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private CookieService cookieService;

    @BeforeEach
    void setUp() {
        // 테스트용 프로파일 설정
        ReflectionTestUtils.setField(cookieService, "activeProfile", "local");
    }

    @Test
    @DisplayName("Refresh Token 쿠키 설정 성공 - 로컬 환경")
    void setRefreshTokenCookie_LocalEnvironment() {
        // Given
        String refreshToken = "refresh_token_value";
        Long expiresInMs = 604800000L;

        // When
        cookieService.setRefreshTokenCookie(response, refreshToken, expiresInMs);

        // Then
        verify(response).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("Refresh Token 쿠키 설정 성공 - 운영 환경")
    void setRefreshTokenCookie_ProductionEnvironment() {
        // Given
        ReflectionTestUtils.setField(cookieService, "activeProfile", "prod");
        String refreshToken = "refresh_token_value";
        Long expiresInMs = 604800000L;

        // When
        cookieService.setRefreshTokenCookie(response, refreshToken, expiresInMs);

        // Then
        verify(response).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("Refresh Token 쿠키 삭제 성공 - 로컬 환경")
    void clearRefreshTokenCookie_LocalEnvironment() {
        // Given & When
        cookieService.clearRefreshTokenCookie(response);

        // Then
        verify(response).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("Refresh Token 쿠키 삭제 성공 - 운영 환경")
    void clearRefreshTokenCookie_ProductionEnvironment() {
        // Given
        ReflectionTestUtils.setField(cookieService, "activeProfile", "prod");

        // When
        cookieService.clearRefreshTokenCookie(response);

        // Then
        verify(response).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("쿠키 설정 - 다양한 만료 시간")
    void setRefreshTokenCookie_VariousExpirationTimes() {
        // Given
        String refreshToken = "refresh_token_value";
        Long shortExpiresInMs = 3600000L;
        Long longExpiresInMs = 2592000000L;

        // When
        cookieService.setRefreshTokenCookie(response, refreshToken, shortExpiresInMs);
        cookieService.setRefreshTokenCookie(response, refreshToken, longExpiresInMs);

        // Then
        verify(response, times(2)).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("쿠키 설정 - 빈 토큰")
    void setRefreshTokenCookie_EmptyToken() {
        // Given
        String emptyToken = "";
        Long expiresInMs = 604800000L;

        // When
        cookieService.setRefreshTokenCookie(response, emptyToken, expiresInMs);

        // Then
        verify(response).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("쿠키 설정 - 긴 토큰")
    void setRefreshTokenCookie_LongToken() {
        // Given
        String longToken = "very_long_refresh_token_value_that_might_be_used_in_production_environment";
        Long expiresInMs = 604800000L;

        // When
        cookieService.setRefreshTokenCookie(response, longToken, expiresInMs);

        // Then
        verify(response).addHeader(eq("Set-Cookie"), anyString());
    }
} 