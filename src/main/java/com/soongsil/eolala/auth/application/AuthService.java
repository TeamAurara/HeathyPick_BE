package com.soongsil.eolala.auth.application;

import com.soongsil.eolala.auth.client.kakao.KakaoApiClient;
import com.soongsil.eolala.auth.client.kakao.KakaoClient;
import com.soongsil.eolala.auth.client.kakao.KakaoProperties;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoTokenResponse;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;
import com.soongsil.eolala.auth.dto.*;
import com.soongsil.eolala.user.application.UserService;
import com.soongsil.eolala.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final KakaoClient kakaoClient;
    private final KakaoApiClient kakaoApiClient;
    private final KakaoProperties kakaoProperties;
    private final UserService userService;
    private final TokenService tokenService;

    @Transactional
    public AuthResponseDto loginWithKakao(AuthRequestDto request) {
        //1) authorization code -> access/refresh token 교환
        KakaoTokenResponse tokenResp = kakaoClient.getToken(
            "authorization_code",
            kakaoProperties.clientId(),
            kakaoProperties.clientSecret(),
            kakaoProperties.redirectUri(),
            request.code()
        );
        
        // 2) access token -> 카카오 사용자 정보 조회
        KakaoUserInfoResponse kakaoUser = kakaoApiClient.getUserInfo("Bearer " + tokenResp.accessToken());
        
        // 3) 사용자 생성/조회 및 JWT 토큰 발급
        User user = userService.findOrCreate(kakaoUser);
        TokenDto tokens = tokenService.createTokens(user);
        UserInfoDto userInfo = UserInfoDto.from(user);
        return new AuthResponseDto(tokens, userInfo);
    }

    @Transactional
    public AccessTokenResponseDto refreshToken(RefreshRequestDto request) {
        RefreshedTokensDto refreshedTokens = tokenService.refreshTokens(request.refreshToken());
        return new AccessTokenResponseDto(
                refreshedTokens.accessToken(),
                refreshedTokens.accessTokenExpiresIn()
        );
    }
    @Transactional
    public void logout(Long userId) {
        tokenService.deleteRefreshToken(userId);
    }
}