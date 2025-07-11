package com.soongsil.eolala.user.application;

import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.UserOnboarding;
import com.soongsil.eolala.user.domain.type.Gender;
import com.soongsil.eolala.user.dto.request.OnboardingRequest;
import com.soongsil.eolala.user.exception.AlreadyOnboardedException;
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
public class OnboardingService {

    private final UserRepository userRepository;

    @Transactional
    public void saveOnboardingInfo(Long userId, OnboardingRequest onboardingRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorType.USER_NOT_FOUND));

        if (user.isOnboarded()) {
            throw new AlreadyOnboardedException(UserErrorType.ALREADY_ONBOARDED);
        }

        user.updateUser(
                onboardingRequest.nickname(),
                Gender.from(onboardingRequest.gender()),
                onboardingRequest.age()
        );
        UserOnboarding onboarding = onboardingRequest.toOnboarding(user);
        user.updateOnboarding(onboarding);
        userRepository.save(user);
    }
}
