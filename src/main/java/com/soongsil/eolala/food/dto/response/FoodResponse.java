package com.soongsil.eolala.food.dto.response;

import com.soongsil.eolala.food.domain.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FoodResponse(
        @Schema(description = "음식 ID", example = "1")
        Long foodId,

        @Schema(description = "음식 이름", example = "감자탕")
        String menuName,

        @Schema(description = "칼로리", example = "500.0")
        Double calories,

        @Schema(description = "단백질", example = "30.0")
        Double protein,

        @Schema(description = "지방", example = "20.0")
        Double fat,

        @Schema(description = "탄수화물", example = "50.0")
        Double carbohydrate
) {
    public static FoodResponse from(Food food) {
        return new FoodResponse(
                food.getId(),
                food.getMenuName(),
                food.getCalorie(),
                food.getProtein(),
                food.getFat(),
                food.getCarbohydrate()
        );
    }
}
