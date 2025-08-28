package com.soongsil.eolala.home.application;

import com.soongsil.eolala.health.application.UserHealthMetricsService;
import com.soongsil.eolala.health.domain.UserHealthMetrics;
import com.soongsil.eolala.home.domain.vo.NutrientSummary;
import com.soongsil.eolala.home.dto.response.HomeResponse;
import com.soongsil.eolala.record.domain.Records;
import com.soongsil.eolala.record.persistence.RecordsRepository;
import com.soongsil.eolala.user.domain.User;
import com.soongsil.eolala.user.exception.UserErrorType;
import com.soongsil.eolala.user.exception.UserNotFoundException;
import com.soongsil.eolala.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final UserRepository userRepository;
    private final RecordsRepository recordsRepository;
    private final UserHealthMetricsService userHealthMetricsService;

    public HomeResponse getHomeSummary(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(UserErrorType.USER_NOT_FOUND));
        
        validateUserOnboarding(user);
        
        List<Records> todayRecords = recordsRepository.findByUserAndCreatedDate(user, LocalDate.now());
        
        NutrientSummary nutrientSummary = NutrientSummary.from(todayRecords);

        UserHealthMetrics healthMetrics = userHealthMetricsService.getUserHealthMetrics(userId);
        
        int dailyCaloriesGoal = calculateDailyCaloriesGoal(healthMetrics);
        int remainingCalories = nutrientSummary.getRemainingCalories(dailyCaloriesGoal);

        log.info("User {} 홈 조회 - 남은 칼로리: {}, 섭취 칼로리: {}",
            userId, remainingCalories, nutrientSummary.totalCalories());
        
        return HomeResponse.of(remainingCalories, dailyCaloriesGoal, nutrientSummary, healthMetrics);
    }
    
    private void validateUserOnboarding(User user) {
        if (!user.isOnboarded()) {
            throw new UserNotFoundException(UserErrorType.USER_NOT_ONBOARDED);
        }
    }
    
    private int calculateDailyCaloriesGoal(UserHealthMetrics healthMetrics) {
        if (healthMetrics == null || healthMetrics.getDailyCalories() == null) {
            return 0;
        }
        return healthMetrics.getDailyCalories();
    }
}