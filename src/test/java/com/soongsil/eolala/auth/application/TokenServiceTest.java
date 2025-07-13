package com.soongsil.eolala.auth.application;

import com.soongsil.eolala.auth.domain.RefreshToken;
import com.soongsil.eolala.auth.dto.RefreshedTokensDto;
import com.soongsil.eolala.auth.dto.TokenDto;
import com.soongsil.eolala.auth.exception.ExpiredTokenException;
import com.soongsil.eolala.auth.exception.InvalidTokenException;
import com.soongsil.eolala.auth.persistence.RefreshTokenRepository;
import com.soongsil.eolala.auth.util.JwtProvider;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;
import com.soongsil.eolala.user.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenService 단위 테스트")
class TokenServiceTest {

    @Mock
    private JwtProvider jwtProvider;
    
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private TokenService tokenService;

    private User testUser;
    private RefreshToken refreshToken;

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

        refreshToken = RefreshToken.builder()
            .userId(1L)
            .refreshToken("refresh_token")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
            .build();
    }

    @Test
    @DisplayName("토큰 생성 성공 - User 객체 사용")
    void createTokens_WithUser_Success() {
        // Given
        given(jwtProvider.generateAccessToken(anyLong(), any())).willReturn("access_token");
        given(jwtProvider.generateRefreshToken(anyLong(), any())).willReturn("refresh_token");
        given(jwtProvider.getAccessTokenValidityMs()).willReturn(3600000L);
        given(jwtProvider.getRefreshTokenValidityMs()).willReturn(604800000L);
        given(refreshTokenRepository.save(any())).willReturn(refreshToken);

        // When
        TokenDto result = tokenService.createTokens(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access_token");
        assertThat(result.refreshToken()).isEqualTo("refresh_token");
        assertThat(result.accessTokenExpiresIn()).isEqualTo(3600000L);
        assertThat(result.refreshTokenExpiresIn()).isEqualTo(604800000L);
        
        verify(refreshTokenRepository).deleteByUserId(any());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("토큰 생성 성공 - userId와 roles 사용")
    void createTokens_WithUserIdAndRoles_Success() {
        // Given
        Long userId = 1L;
        List<String> roles = List.of("ROLE_USER");
        
        given(jwtProvider.generateAccessToken(anyLong(), anyList())).willReturn("access_token");
        given(jwtProvider.generateRefreshToken(anyLong(), anyList())).willReturn("refresh_token");
        given(jwtProvider.getAccessTokenValidityMs()).willReturn(3600000L);
        given(jwtProvider.getRefreshTokenValidityMs()).willReturn(604800000L);
        given(refreshTokenRepository.save(any())).willReturn(refreshToken);

        // When
        TokenDto result = tokenService.createTokens(userId, roles);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access_token");
        assertThat(result.refreshToken()).isEqualTo("refresh_token");
        
        verify(refreshTokenRepository).deleteByUserId(userId);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    void refreshTokens_Success() {
        // Given
        String refreshTokenStr = "refresh_token";
        
        given(refreshTokenRepository.findByRefreshToken(refreshTokenStr))
            .willReturn(Optional.of(refreshToken));
        given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
        given(jwtProvider.generateAccessToken(anyLong(), any())).willReturn("new_access_token");
        given(jwtProvider.generateRefreshToken(anyLong(), any())).willReturn("new_refresh_token");
        given(jwtProvider.getAccessTokenValidityMs()).willReturn(3600000L);
        given(jwtProvider.getRefreshTokenValidityMs()).willReturn(604800000L);
        given(refreshTokenRepository.save(any())).willReturn(refreshToken);

        // When
        RefreshedTokensDto result = tokenService.refreshTokens(refreshTokenStr);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("new_access_token");
        assertThat(result.refreshToken()).isEqualTo("new_refresh_token");
        assertThat(result.userId()).isEqualTo(1L);
        
        verify(refreshTokenRepository).findByRefreshToken(refreshTokenStr);
        verify(userRepository).findById(1L);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 존재하지 않는 토큰")
    void refreshTokens_NotFound() {
        // Given
        String refreshTokenStr = "invalid_token";
        given(refreshTokenRepository.findByRefreshToken(refreshTokenStr))
            .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> tokenService.refreshTokens(refreshTokenStr))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 만료된 토큰")
    void refreshTokens_ExpiredToken() {
        // Given
        String refreshTokenStr = "expired_token";
        RefreshToken expiredToken = RefreshToken.builder()
            .userId(1L)
            .refreshToken(refreshTokenStr)
            .issuedAt(Instant.now().minus(10, ChronoUnit.DAYS))
            .expiresAt(Instant.now().minus(1, ChronoUnit.DAYS))
            .build();
        
        given(refreshTokenRepository.findByRefreshToken(refreshTokenStr))
            .willReturn(Optional.of(expiredToken));

        // When & Then
        assertThatThrownBy(() -> tokenService.refreshTokens(refreshTokenStr))
            .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 존재하지 않는 사용자")
    void refreshTokens_UserNotFound() {
        // Given
        String refreshTokenStr = "refresh_token";
        given(refreshTokenRepository.findByRefreshToken(refreshTokenStr))
            .willReturn(Optional.of(refreshToken));
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> tokenService.refreshTokens(refreshTokenStr))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("리프레시 토큰 삭제 성공")
    void deleteRefreshToken_Success() {
        // Given
        Long userId = 1L;

        // When
        tokenService.deleteRefreshToken(userId);

        // Then
        verify(refreshTokenRepository).deleteByUserId(userId);
    }

    @Test
    @DisplayName("사용자 권한 조회 성공")
    void getUserRoles_Success() {
        // When
        List<String> roles = tokenService.getUserRoles(testUser);

        // Then
        assertThat(roles).containsExactly("ROLE_USER");
    }
} 