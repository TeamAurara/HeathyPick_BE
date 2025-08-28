package com.soongsil.eolala.home.application;

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

    public HomeResponse getHomeSummary(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(UserErrorType.USER_NOT_FOUND));
        
        validateUserOnboarding(user);
        
        List<Records> todayRecords = recordsRepository.findByUserAndCreatedDate(user, LocalDate.now());
        
        NutrientSummary nutrientSummary = NutrientSummary.from(todayRecords);
        
        Integer dailyCalories = user.getDailyCalories();
        int remainingCalories = nutrientSummary.getRemainingCalories(dailyCalories);
        
        log.info("User {} 홈 조회 - 남은 칼로리: {}, 섭취 칼로리: {}", 
            userId, remainingCalories, nutrientSummary.totalCalories());
        
        return HomeResponse.of(remainingCalories, dailyCalories != null ? dailyCalories : 0, nutrientSummary);
    }
    
    private void validateUserOnboarding(User user) {
        if (!user.isOnboarded()) {
            log.warn("User {}가 온보딩을 완료하지 않았습니다.", user.getId());
            throw new UserNotFoundException(UserErrorType.USER_NOT_ONBOARDED);
        }
        
        if (user.getDailyCalories() == null) {
            log.warn("User {}의 일일 적정 칼로리가 설정되지 않았습니다.", user.getId());
        }
    }
}