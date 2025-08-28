package com.soongsil.eolala.home.dto.response;

import com.soongsil.eolala.health.domain.UserHealthMetrics;
import com.soongsil.eolala.home.domain.vo.NutrientSummary;
import lombok.Builder;

import static com.soongsil.eolala.common.constants.NutritionConstants.NutrientNames.*;
import static com.soongsil.eolala.common.constants.NutritionConstants.Units.*;

@Builder
public record HomeResponse(
    CaloriesInfo calories,
    NutrientsInfo nutrients
) {
    
    public static HomeResponse of(int remainingCalories, int dailyCaloriesGoal, 
                                  NutrientSummary nutrientSummary, UserHealthMetrics healthMetrics) {
        return HomeResponse.builder()
            .calories(CaloriesInfo.of(remainingCalories, dailyCaloriesGoal, nutrientSummary.totalCalories()))
            .nutrients(NutrientsInfo.from(nutrientSummary, healthMetrics))
            .build();
    }

    @Builder
    public record CaloriesInfo(
        int remainingCalories,
        int dailyGoal,
        int consumed
    ) {
        public static CaloriesInfo of(int remainingCalories, int dailyGoal, double consumed) {
            return CaloriesInfo.builder()
                .remainingCalories(remainingCalories)
                .dailyGoal(dailyGoal)
                .consumed((int) consumed)
                .build();
        }
    }

    @Builder
    public record NutrientsInfo(
        NutrientDetail carbohydrate,
        NutrientDetail protein,
        NutrientDetail fat,
        NutrientDetail sodium,
        NutrientDetail potassium,
        NutrientDetail phosphate
    ) {
        public static NutrientsInfo from(NutrientSummary summary, UserHealthMetrics metrics) {
            return NutrientsInfo.builder()
                .carbohydrate(NutrientDetail.of(CARBOHYDRATE, summary.totalCarbohydrate(), 
                    metrics != null ? metrics.getDailyCarbohydrate() : null, GRAM))
                .protein(NutrientDetail.of(PROTEIN, summary.totalProtein(), 
                    metrics != null ? metrics.getDailyProtein() : null, GRAM))
                .fat(NutrientDetail.of(FAT, summary.totalFat(), 
                    metrics != null ? metrics.getDailyFat() : null, GRAM))
                .sodium(NutrientDetail.of(SODIUM, summary.totalSodium(), 
                    metrics != null ? metrics.getDailySodium() : null, MILLIGRAM))
                .potassium(NutrientDetail.of(POTASSIUM, summary.totalPotassium(), 
                    metrics != null ? metrics.getDailyPotassium() : null, MILLIGRAM))
                .phosphate(NutrientDetail.of(PHOSPHATE, summary.totalPhosphate(), 
                    metrics != null ? metrics.getDailyPhosphate() : null, MILLIGRAM))
                .build();
        }
    }

    @Builder
    public record NutrientDetail(
        String name,
        double amount,
        Integer dailyLimit,  // 일일 제한량 (null이면 제한 없음)
        String unit
    ) {
        public static NutrientDetail of(String name, double amount, String unit) {
            return of(name, amount, null, unit);
        }
        
        public static NutrientDetail of(String name, double amount, Integer dailyLimit, String unit) {
            return NutrientDetail.builder()
                .name(name)
                .amount(amount)
                .dailyLimit(dailyLimit)
                .unit(unit)
                .build();
        }

        public Integer getRemainingAmount() {
            if (dailyLimit == null) return null;
            return Math.max(0, dailyLimit - (int)amount);
        }
    }
}