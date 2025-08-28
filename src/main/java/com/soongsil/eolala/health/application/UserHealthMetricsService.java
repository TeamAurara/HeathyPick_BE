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
        int dailyCarbohydrate = onboarding.calculateDailyCarbohydrateLimit();
        int dailyProtein = onboarding.calculateDailyProteinLimit();
        int dailyFat = onboarding.calculateDailyFatLimit();
        int dailySodium = onboarding.calculateDailySodiumLimit();
        int dailyPotassium = onboarding.calculateDailyPotassiumLimit();
        int dailyPhosphate = onboarding.calculateDailyPhosphateLimit();
        
        Integer potassiumLimit = dailyPotassium > 0 ? dailyPotassium : null;
        Integer phosphateLimit = dailyPhosphate > 0 ? dailyPhosphate : null;
        
        return userHealthMetricsRepository.findByUser(user)
            .map(existingMetrics -> {
                existingMetrics.updateMetrics(
                    dailyCalories, dailyCarbohydrate, dailyProtein, 
                    dailyFat, dailySodium, potassiumLimit, phosphateLimit
                );
                log.info("User {} 건강 지표 업데이트 - 칼로리: {}, 단백질: {}g, 나트륨: {}mg",
                    user.getId(), dailyCalories, dailyProtein, dailySodium);
                return existingMetrics;
            })
            .orElseGet(() -> {
                UserHealthMetrics newMetrics = UserHealthMetrics.builder()
                    .user(user)
                    .dailyCalories(dailyCalories)
                    .dailyCarbohydrate(dailyCarbohydrate)
                    .dailyProtein(dailyProtein)
                    .dailyFat(dailyFat)
                    .dailySodium(dailySodium)
                    .dailyPotassium(potassiumLimit)
                    .dailyPhosphate(phosphateLimit)
                    .build();
                log.info("User {} 건강 지표 신규 저장 - CKD Level: {}, 칼로리: {}",
                    user.getId(), onboarding.getCkdLevel(), dailyCalories);
                return userHealthMetricsRepository.save(newMetrics);
            });
    }


    public Integer getUserDailyCalories(Long userId) {
        return userHealthMetricsRepository.findByUserId(userId)
            .map(UserHealthMetrics::getDailyCalories)
            .orElse(null);
    }

    public UserHealthMetrics getUserHealthMetrics(Long userId) {
        return userHealthMetricsRepository.findByUserId(userId)
            .orElse(null);
    }
}