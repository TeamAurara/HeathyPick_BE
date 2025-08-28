package com.soongsil.eolala.health.application;

import com.soongsil.eolala.health.domain.UserHealthMetrics;
import com.soongsil.eolala.health.persistence.UserHealthMetricsRepository;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.domain.UserOnboarding;
import com.soongsil.eolala.user.exception.UserErrorType;
import com.soongsil.eolala.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserHealthMetricsService {
    
    private final UserHealthMetricsRepository userHealthMetricsRepository;

    @Transactional
    public UserHealthMetrics calculateAndSaveMetrics(User user) {
        UserOnboarding onboarding = user.getOnboarding();
        if (onboarding == null) {
            throw new UserNotFoundException(UserErrorType.USER_NOT_ONBOARDED);
        }
        
        int dailyCalories = onboarding.calculateDailyCalories();
        
        return userHealthMetricsRepository.findByUser(user)
            .map(existingMetrics -> {
                existingMetrics.updateMetrics(dailyCalories);
                return existingMetrics;
            })
            .orElseGet(() -> {
                UserHealthMetrics newMetrics = UserHealthMetrics.builder()
                    .user(user)
                    .dailyCalories(dailyCalories)
                    .build();
                return userHealthMetricsRepository.save(newMetrics);
            });
    }


    public Integer getUserDailyCalories(Long userId) {
        return userHealthMetricsRepository.findByUserId(userId)
            .map(UserHealthMetrics::getDailyCalories)
            .orElse(null);
    }
}