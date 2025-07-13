package com.soongsil.eolala.auth.application;

import com.soongsil.eolala.auth.client.kakao.KakaoClient;
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
    private final UserService userService;
    private final TokenService tokenService;

    @Transactional
    public AuthResponseDto loginWithKakao(AuthRequestDto request) {
        KakaoTokenResponse tokenResp = kakaoClient.getToken(request.code());
        KakaoUserInfoResponse kakaoUser = kakaoClient.getUserInfo(tokenResp.accessToken());
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