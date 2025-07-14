package com.soongsil.eolala.user.application;

import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;
import com.soongsil.eolala.user.exception.UserErrorType;
import com.soongsil.eolala.user.exception.UserNotFoundException;
import com.soongsil.eolala.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User findOrCreate(KakaoUserInfoResponse kakaoUser) {
        String providerId = kakaoUser.getId().toString();

        return userRepository.findByProviderId(providerId)
                .map(existingUser -> {
                    log.info("기존 사용자 조회 완료 - providerId: {}", providerId);
                    return existingUser;
                })
                .orElseGet(() -> {
                    log.info("새 사용자 생성 시작 - providerId: {}", providerId);
                    return createNewUser(kakaoUser);
                });
    }

    private User createNewUser(KakaoUserInfoResponse kakaoUser) {
        String providerId = kakaoUser.getId().toString();
        String email = kakaoUser.getEmail();

        log.info("새 사용자 생성 시작 - providerId: {}, email: {}", providerId, email);
        User newUser = User.builder()
                .email(email)
                .providerId(providerId)
                .socialType(SocialType.KAKAO)
                .nickname(kakaoUser.getNickname())
                .gender(Gender.MALE) // 기본값.온보딩에서 수정 가능
                .age(0) // 기본값.온보딩에서 수정 가능
                .profileImageUrl(kakaoUser.account().profile().profileImageUrl())
                .isOnboarded(false)
                .role(Role.USER) // 기본 역할:USER
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("새 사용자 생성 완료 - userId: {}, providerId: {}", savedUser.getId(), savedUser.getProviderId());
        return savedUser;
    }

    public User getUser (Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorType.USER_NOT_FOUND));
    }
}

