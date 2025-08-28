package com.soongsil.eolala.home.dto.response;

import com.soongsil.eolala.home.domain.vo.NutrientSummary;
import lombok.Builder;

import static com.soongsil.eolala.common.constants.NutritionConstants.NutrientNames.*;
import static com.soongsil.eolala.common.constants.NutritionConstants.Units.*;

@Builder
public record HomeResponse(
    CaloriesInfo calories,
    NutrientsInfo nutrients
) {
    
    public static HomeResponse of(int remainingCalories, int dailyCaloriesGoal, NutrientSummary nutrientSummary) {
        return HomeResponse.builder()
            .calories(CaloriesInfo.of(remainingCalories, dailyCaloriesGoal, nutrientSummary.totalCalories()))
            .nutrients(NutrientsInfo.from(nutrientSummary))
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
        public static NutrientsInfo from(NutrientSummary summary) {
            return NutrientsInfo.builder()
                .carbohydrate(NutrientDetail.of(CARBOHYDRATE, summary.totalCarbohydrate(), GRAM))
                .protein(NutrientDetail.of(PROTEIN, summary.totalProtein(), GRAM))
                .fat(NutrientDetail.of(FAT, summary.totalFat(), GRAM))
                .sodium(NutrientDetail.of(SODIUM, summary.totalSodium(), MILLIGRAM))
                .potassium(NutrientDetail.of(POTASSIUM, summary.totalPotassium(), MILLIGRAM))
                .phosphate(NutrientDetail.of(PHOSPHATE, summary.totalPhosphate(), MILLIGRAM))
                .build();
        }
    }

    @Builder
    public record NutrientDetail(
        String name,
        double amount,
        String unit
    ) {
        public static NutrientDetail of(String name, double amount, String unit) {
            return NutrientDetail.builder()
                .name(name)
                .amount(amount)
                .unit(unit)
                .build();
        }
    }
}