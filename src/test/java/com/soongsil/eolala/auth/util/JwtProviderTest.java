package com.soongsil.eolala.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;


import javax.crypto.SecretKey;
import java.util.List;

import com.soongsil.eolala.auth.exception.InvalidTokenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("JwtProvider 단위 테스트")
class JwtProviderTest {

    private JwtProvider jwtProvider;
    private SecretKey key;
    private final String secretKey = "2ad81fbb547b1c455cb3e4e80b384d81d51d739094795ceb69d83f0af9008dd8";
    private final long accessTokenValiditySeconds = 900; // 15분
    private final long refreshTokenValiditySeconds = 604800; // 7일

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(secretKey, accessTokenValiditySeconds, refreshTokenValiditySeconds);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    @DisplayName("Access Token 생성 성공")
    void generateAccessToken_Success() {
        // Given
        Long userId = 1L;
        List<String> roles = List.of("ROLE_USER");

        // When
        String accessToken = jwtProvider.generateAccessToken(userId, roles);

        // Then
        assertThat(accessToken).isNotNull();
        assertThat(accessToken).isNotEmpty();
        
        // JWT 토큰 파싱하여 내용 검증
        Jws<Claims> jws = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(accessToken);
        
        Claims claims = jws.getPayload();
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("roles")).isEqualTo(roles);
        assertThat(claims.get("type")).isEqualTo("access");
    }

    @Test
    @DisplayName("Refresh Token 생성 성공")
    void generateRefreshToken_Success() {
        // Given
        Long userId = 1L;
        List<String> roles = List.of("ROLE_USER");

        // When
        String refreshToken = jwtProvider.generateRefreshToken(userId, roles);

        // Then
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isNotEmpty();
        
        // JWT 토큰 파싱하여 내용 검증
        Jws<Claims> jws = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(refreshToken);
        
        Claims claims = jws.getPayload();
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("roles")).isEqualTo(roles);
        assertThat(claims.get("type")).isEqualTo("refresh");
    }

    @Test
    @DisplayName("Authentication 객체 생성 성공")
    void getAuthentication_Success() {
        // Given
        Long userId = 1L;
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");
        String accessToken = jwtProvider.generateAccessToken(userId, roles);

        // When
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("1");
        assertThat(authentication.getAuthorities()).hasSize(2);
        assertThat(authentication.getAuthorities().stream()
            .map(Object::toString)
            .toList()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("토큰 검증 성공")
    void validateToken_Success() {
        // Given
        Long userId = 1L;
        List<String> roles = List.of("ROLE_USER");
        String accessToken = jwtProvider.generateAccessToken(userId, roles);

        // When & Then
        assertDoesNotThrow(() -> jwtProvider.validateToken(accessToken));
    }

    @Test
    @DisplayName("토큰 검증 실패 - 잘못된 토큰")
    void validateToken_InvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThatThrownBy(() -> jwtProvider.validateToken(invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰 검증 실패 - null 토큰")
    void validateToken_NullToken() {
        // Given
        String nullToken = null;

        // When
        assertThatThrownBy(() -> jwtProvider.validateToken(nullToken))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("Access Token 유효시간 조회")
    void getAccessTokenValidityMs() {
        // When
        long validityMs = jwtProvider.getAccessTokenValidityMs();

        // Then
        assertThat(validityMs).isEqualTo(accessTokenValiditySeconds * 1000);
    }

    @Test
    @DisplayName("Refresh Token 유효시간 조회")
    void getRefreshTokenValidityMs() {
        // When
        long validityMs = jwtProvider.getRefreshTokenValidityMs();

        // Then
        assertThat(validityMs).isEqualTo(refreshTokenValiditySeconds * 1000);
    }

    @Test
    @DisplayName("다른 사용자의 토큰 구분 확인")
    void generateToken_DifferentUsers() {
        // Given
        Long userId1 = 1L;
        Long userId2 = 2L;
        List<String> roles = List.of("ROLE_USER");

        // When
        String token1 = jwtProvider.generateAccessToken(userId1, roles);
        String token2 = jwtProvider.generateAccessToken(userId2, roles);

        // Then
        assertThat(token1).isNotEqualTo(token2);
        
        Authentication auth1 = jwtProvider.getAuthentication(token1);
        Authentication auth2 = jwtProvider.getAuthentication(token2);
        
        assertThat(auth1.getName()).isEqualTo("1");
        assertThat(auth2.getName()).isEqualTo("2");
    }

    @Test
    @DisplayName("다른 권한의 토큰 구분 확인")
    void generateToken_DifferentRoles() {
        // Given
        Long userId = 1L;
        List<String> userRoles = List.of("ROLE_USER");
        List<String> adminRoles = List.of("ROLE_ADMIN");

        // When
        String userToken = jwtProvider.generateAccessToken(userId, userRoles);
        String adminToken = jwtProvider.generateAccessToken(userId, adminRoles);

        // Then
        assertThat(userToken).isNotEqualTo(adminToken);
        
        Authentication userAuth = jwtProvider.getAuthentication(userToken);
        Authentication adminAuth = jwtProvider.getAuthentication(adminToken);
        
        assertThat(userAuth.getAuthorities().stream()
            .map(Object::toString)
            .toList()).contains("ROLE_USER");
        assertThat(adminAuth.getAuthorities().stream()
            .map(Object::toString)
            .toList()).contains("ROLE_ADMIN");
    }
} 