package com.soongsil.eolala.auth.application;

import com.soongsil.eolala.auth.domain.RefreshToken;
import com.soongsil.eolala.auth.dto.TokenDto;
import com.soongsil.eolala.auth.exception.ExpiredTokenException;
import com.soongsil.eolala.auth.exception.InvalidTokenException;
import com.soongsil.eolala.auth.persistence.RefreshTokenRepository;
import com.soongsil.eolala.auth.util.JwtProvider;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    /**
     * 로그인 시 토큰 생성
     */
    @Transactional
    public TokenDto createTokens(Long userId, List<String> roles) {
        log.info("토큰 생성 시작 - userId: {}", userId);
        
        String accessToken = jwtProvider.generateAccessToken(userId, roles);
        
        String refreshToken = jwtProvider.generateRefreshToken(userId, roles);
        long refreshExpiresInMs = jwtProvider.getRefreshTokenValidityMs();
        
        refreshTokenRepository.deleteByUserId(userId);
        
        RefreshToken entity = RefreshToken.builder()
            .userId(userId)
            .refreshToken(refreshToken)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(refreshExpiresInMs))
            .build();
        refreshTokenRepository.save(entity);
        
        log.info("토큰 생성 완료 - userId: {}", userId);
        
        return new TokenDto(
            accessToken,
            refreshToken,
            jwtProvider.getAccessTokenValidityMs(),
            refreshExpiresInMs
        );
    }

    /**
     * 로그인 시 토큰 생성
     */
    @Transactional
    public TokenDto createTokens(User user) {
        List<String> roles = getUserRoles(user);
        return createTokens(user.getId(), roles);
    }

    /**
     * 리프레시 토큰 갱신
     */
    @Transactional
    public TokenDto refreshTokens(String refreshToken) {
        log.info("토큰 갱신 시작 - refreshToken: {}", refreshToken.substring(0, 10) + "...");

        RefreshToken stored = refreshTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> {
                log.warn("유효하지 않은 RefreshToken: {}", refreshToken.substring(0, 10) + "...");
                return new InvalidTokenException();
            });

        if (stored.isExpired()) {
            log.warn("만료된 RefreshToken - userId: {}", stored.getUserId());
            throw new ExpiredTokenException();
        }

        User user = userRepository.findById(stored.getUserId())
            .orElseThrow(() -> {
                log.warn("사용자를 찾을 수 없음 - userId: {}", stored.getUserId());
                return new InvalidTokenException();
            });
        
        List<String> roles = getUserRoles(user);
        
        String newAccessToken = jwtProvider.generateAccessToken(stored.getUserId(), roles);
        String newRefreshToken = jwtProvider.generateRefreshToken(stored.getUserId(), roles);
        long refreshExpiresInMs = jwtProvider.getRefreshTokenValidityMs();

        stored.updateToken(
            newRefreshToken,
            Instant.now(),
            Instant.now().plusMillis(refreshExpiresInMs)
        );
        refreshTokenRepository.save(stored);
        
        log.info("토큰 갱신 완료 - userId: {}", stored.getUserId());

        return new TokenDto(
            newAccessToken,
            newRefreshToken,
            jwtProvider.getAccessTokenValidityMs(),
            refreshExpiresInMs
        );
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        log.info("RefreshToken 삭제 - userId: {}", userId);
        refreshTokenRepository.deleteByUserId(userId);
    }

    public List<String> getUserRoles(User user) {
        return List.of("ROLE_" + user.getRole().name());
    }
}
