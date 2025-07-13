package com.soongsil.eolala.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongsil.eolala.auth.application.AuthService;
import com.soongsil.eolala.auth.application.CookieService;
import com.soongsil.eolala.auth.dto.AuthRequestDto;
import com.soongsil.eolala.auth.dto.*;
import com.soongsil.eolala.auth.dto.UserInfoDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.soongsil.eolala.auth.filter.JwtFilter;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import com.soongsil.eolala.auth.config.TestSecurityConfig;



import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class))
@ContextConfiguration(classes = {AuthController.class, TestSecurityConfig.class})
@DisplayName("AuthController 단위 테스트")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

	@MockitoBean
    private CookieService cookieService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthRequestDto authRequestDto;
    private AuthResponseDto authResponseDto;
    private TokenDto tokenDto;
    private UserInfoDto userInfoDto;

    @BeforeEach
    void setUp() {
        authRequestDto = new AuthRequestDto("kakao_authorization_code");

        tokenDto = new TokenDto(
            "access_token",
            "refresh_token",
            3600000L,
            604800000L
        );

        userInfoDto = new UserInfoDto(1L, "test@test.com", "testUser", "profile.jpg", "MALE", 25, false);

        authResponseDto = new AuthResponseDto(tokenDto, userInfoDto);
    }

    @Test
    @DisplayName("카카오 로그인 성공")
    void loginWithKakao_Success() throws Exception {
        // Given
                given(authService.loginWithKakao(any(AuthRequestDto.class))).willReturn(authResponseDto);
        doNothing().when(cookieService).setRefreshTokenCookie(any(), anyString(), anyLong());

        // When & Then
        mockMvc.perform(post("/api/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
                        .andExpect(jsonPath("$.data.token.accessToken").value("access_token"))
            .andExpect(jsonPath("$.data.token.refreshToken").doesNotExist())
            .andExpect(jsonPath("$.data.user.userId").value(1))
            .andExpect(jsonPath("$.data.user.email").value("test@test.com"))
            .andExpect(jsonPath("$.data.user.nickname").value("testUser"));

        verify(authService).loginWithKakao(any(AuthRequestDto.class));
        verify(cookieService).setRefreshTokenCookie(any(), anyString(), anyLong());
    }

    @Test
    @DisplayName("카카오 로그인 실패 - 잘못된 요청")
    void loginWithKakao_InvalidRequest() throws Exception {
        // Given
        AuthRequestDto invalidRequest = new AuthRequestDto("");

        // When & Then
        mockMvc.perform(post("/api/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    void refreshToken_Success() throws Exception {
        // Given
        AccessTokenResponseDto accessTokenResponseDto = new AccessTokenResponseDto("new_access_token", 3600000L);
        given(authService.refreshToken(any(RefreshRequestDto.class))).willReturn(accessTokenResponseDto);

        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                .cookie(new jakarta.servlet.http.Cookie("refresh_token", "refresh_token_value")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.access_token").value("new_access_token"))
            .andExpect(jsonPath("$.data.expires_in").value(3600000L));

        verify(authService).refreshToken(any(RefreshRequestDto.class));
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 리프레시 토큰 없음")
    void refreshToken_MissingRefreshToken() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/refresh"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 빈 리프레시 토큰")
    void refreshToken_EmptyRefreshToken() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                .cookie(new jakarta.servlet.http.Cookie("refresh_token", "")))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_Success() throws Exception {
        // Given
        doNothing().when(authService).logout(1L);
        doNothing().when(cookieService).clearRefreshTokenCookie(any());

        // When & Then
        mockMvc.perform(post("/api/auth/logout")
                .with(user("1").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").doesNotExist());

        verify(authService).logout(1L);
        verify(cookieService).clearRefreshTokenCookie(any());
    }

    @Test
    @DisplayName("로그아웃 실패 - 인증되지 않은 사용자")
    void logout_Unauthorized() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/logout"))
            .andExpect(status().isUnauthorized());
    }
} 