
package com.soongsil.eolala.auth.application;

import com.soongsil.eolala.auth.client.kakao.KakaoClient;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoTokenResponse;
import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;
import com.soongsil.eolala.auth.dto.RefreshedTokensDto;
import com.soongsil.eolala.auth.dto.AuthRequestDto;
import com.soongsil.eolala.auth.dto.AuthResponseDto;
import com.soongsil.eolala.auth.dto.RefreshRequestDto;
import com.soongsil.eolala.auth.dto.TokenDto;
import com.soongsil.eolala.auth.dto.UserInfoDto;

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

		UserInfoDto userInfo = new UserInfoDto(
			user.getId(),
			user.getEmail(),
			user.getNickname(),
			user.getProfileImageUrl(),
			user.getGender(),
			user.getAge(),
			user.isOnboarded()
		);

		return new AuthResponseDto(tokens, userInfo);
	}

	@Transactional
	public AuthResponseDto refreshToken(RefreshRequestDto request) {

		RefreshedTokensDto refreshedTokens = tokenService.refreshTokens(request.refreshToken());

		User user = userService.findById(refreshedTokens.userId());

		TokenDto newTokens = new TokenDto(
			refreshedTokens.accessToken(),
			refreshedTokens.refreshToken(),
			refreshedTokens.accessTokenExpiresIn(),
			refreshedTokens.refreshTokenExpiresIn()
		);

		UserInfoDto userInfo = new UserInfoDto(
			user.getId(),
			user.getEmail(),
			user.getNickname(),
			user.getProfileImageUrl(),
			user.getGender(),
			user.getAge(),
			user.isOnboarded()
		);

		return new AuthResponseDto(newTokens, userInfo);
	}

	@Transactional
	public void logout(Long userId) {
		tokenService.deleteRefreshToken(userId);
	}

}