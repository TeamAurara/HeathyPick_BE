package com.soongsil.eolala.user.application;

import com.soongsil.eolala.auth.client.kakao.dto.KakaoUserInfoResponse;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.domain.type.Role;
import com.soongsil.eolala.user.domain.type.SocialType;
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
        // 카카오 사용자 정보를 기반으로 사용자 조회 또는 생성
        return userRepository.findByEmail(kakaoUser.getEmail())
            .map(existingUser -> {
                log.info("기존 사용자 조회 완료 - email: {}", kakaoUser.getEmail());
                return existingUser;
            })
            .orElseGet(() -> {
                log.info("새 사용자 생성 시작 - email: {}", kakaoUser.getEmail());
                User newUser = User.builder()
                    .email(kakaoUser.getEmail())
                    .socialType(SocialType.KAKAO)
                    .nickname(kakaoUser.getNickname())
                    .gender(Gender.MALE) // 기본값: 온보딩에서 수정 가능
                    .age(0) // 기본값: 온보딩에서 수정 가능
                    .profileImageUrl(kakaoUser.account().profile().profileImageUrl())
                    .isOnboarded(false)
                    .role(Role.USER) // 기본 역할: USER
                    .build();
                
                User savedUser = userRepository.save(newUser);
                log.info("새 사용자 생성 완료 - userId: {}, email: {}", savedUser.getId(), savedUser.getEmail());
                return savedUser;
            });
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("사용자를 찾을 수 없음 - userId: {}", userId);
                return new RuntimeException("User not found with id: " + userId);
            });
    }
} 