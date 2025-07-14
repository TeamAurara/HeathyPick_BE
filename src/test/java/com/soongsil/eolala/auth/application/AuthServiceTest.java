package com.soongsil.eolala.auth.application;

import com.soongsil.eolala.auth.dto.AccessTokenResponseDto;
import com.soongsil.eolala.auth.client.kakao.KakaoApiClient;
import com.soongsil.eolala.auth.client.kakao.KakaoClient;
import com.soongsil.eolala.auth.client.kakao.KakaoProperties;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoTokenResponse;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoAccount;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoProfile;
import com.soongsil.eolala.auth.dto.AuthRequestDto;
import com.soongsil.eolala.auth.dto.AuthResponseDto;
import com.soongsil.eolala.auth.dto.RefreshRequestDto;
import com.soongsil.eolala.auth.dto.TokenDto;
import com.soongsil.eolala.auth.dto.RefreshedTokensDto;
import com.soongsil.eolala.user.application.UserService;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 단위 테스트")
class AuthServiceTest {

    @Mock
    private KakaoClient kakaoClient;
    
    @Mock
    private KakaoApiClient kakaoApiClient;
    
    @Mock
    private KakaoProperties kakaoProperties;
    
    @Mock
    private UserService userService;
    
    @Mock
    private TokenService tokenService;
    
    @InjectMocks
    private AuthService authService;

    private User testUser;
    private KakaoTokenResponse kakaoTokenResponse;
    private KakaoUserInfoResponse kakaoUserInfoResponse;
    private TokenDto tokenDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .email("test@test.com")
            .nickname("TestUser")
            .profileImageUrl("https://example.com/profile.jpg")
            .gender(Gender.MALE)
            .age(25)
            .socialType(SocialType.KAKAO)
            .role(Role.USER)
            .isOnboarded(false)
            .build();
        ReflectionTestUtils.setField(testUser, "id", 1L);

        kakaoTokenResponse = new KakaoTokenResponse(
            "kakao_access_token",
            "kakao_refresh_token",
            3600L,
            604800L
        );

        KakaoProfile kakaoProfile = new KakaoProfile(
            "TestUser",
            "https://example.com/profile.jpg"
        );

        KakaoAccount kakaoAccount = new KakaoAccount(
            kakaoProfile,
            "test@test.com"
        );

        kakaoUserInfoResponse = new KakaoUserInfoResponse(
            12345L,
            kakaoAccount
        );

        tokenDto = new TokenDto(
            "access_token",
            "refresh_token",
            3600000L,
            604800000L
        );
    }

    @Test
    @DisplayName("카카오 로그인 성공")
    void loginWithKakao_Success() {
        // Given
        AuthRequestDto authRequest = new AuthRequestDto("authorization_code");
        
        given(kakaoProperties.clientId()).willReturn("test_client_id");
        given(kakaoProperties.clientSecret()).willReturn("test_client_secret");
        given(kakaoProperties.redirectUri()).willReturn("http://localhost:8080/login/oauth2/code/kakao");
        given(kakaoClient.getToken(anyString(), anyString(), anyString(), anyString(), anyString())).willReturn(kakaoTokenResponse);
        given(kakaoApiClient.getUserInfo(anyString())).willReturn(kakaoUserInfoResponse);
        given(userService.findOrCreate(any())).willReturn(testUser);
        given(tokenService.createTokens(any(User.class))).willReturn(tokenDto);

        // When
        AuthResponseDto result = authService.loginWithKakao(authRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.token().accessToken()).isEqualTo("access_token");
        assertThat(result.token().refreshToken()).isEqualTo("refresh_token");
        assertThat(result.user().userId()).isNotNull();
        assertThat(result.user().email()).isEqualTo("test@test.com");
        assertThat(result.user().nickname()).isEqualTo("TestUser");
        
        verify(kakaoClient).getToken("authorization_code", "test_client_id", "test_client_secret", "http://localhost:8080/login/oauth2/code/kakao", "authorization_code");
        verify(kakaoApiClient).getUserInfo("Bearer kakao_access_token");
        verify(userService).findOrCreate(kakaoUserInfoResponse);
        verify(tokenService).createTokens(testUser);
    }

    @Test
    @DisplayName("리프레시 토큰으로 토큰 재발급 성공")
    void refreshToken_Success() {
        // Given
        String refreshToken = "valid_refresh_token";
        RefreshedTokensDto refreshedTokensDto = new RefreshedTokensDto("new_access_token", "new_refresh_token", 3600000L, 604800000L, 1L);
        given(tokenService.refreshTokens(refreshToken)).willReturn(refreshedTokensDto);

        // When
        AccessTokenResponseDto result = authService.refreshToken(new RefreshRequestDto(refreshToken));

        // Then
        assertNotNull(result);
        assertEquals("new_access_token", result.accessToken());
        assertEquals(3600000L, result.expiresIn());
        verify(tokenService).refreshTokens(refreshToken);
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_Success() {
        // Given
        Long userId = 1L;

        // When
        authService.logout(userId);

        // Then
        verify(tokenService).deleteRefreshToken(userId);
    }
} 