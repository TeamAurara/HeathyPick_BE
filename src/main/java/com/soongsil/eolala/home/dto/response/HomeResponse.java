package com.soongsil.eolala.home.dto.response;

import com.soongsil.eolala.home.domain.vo.NutrientSummary;
import lombok.Builder;

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
                .carbohydrate(NutrientDetail.of("탄수화물", summary.totalCarbohydrate(), "g"))
                .protein(NutrientDetail.of("단백질", summary.totalProtein(), "g"))
                .fat(NutrientDetail.of("지방", summary.totalFat(), "g"))
                .sodium(NutrientDetail.of("나트륨", summary.totalSodium(), "mg"))
                .potassium(NutrientDetail.of("칼륨", summary.totalPotassium(), "mg"))
                .phosphate(NutrientDetail.of("인", summary.totalPhosphate(), "mg"))
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