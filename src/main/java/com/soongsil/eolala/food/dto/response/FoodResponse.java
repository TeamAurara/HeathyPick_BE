package com.soongsil.eolala.food.dto.response;

import com.soongsil.eolala.food.domain.Food;
import lombok.Builder;

@Builder
public record FoodResponse(
        String menuName,
        Double calories,   //칼로리
        Double protein, //단백질
        Double fat, //지방
        Double carbohydrate //탄수화물
) {
    public static FoodResponse from(Food food) {
        return new FoodResponse(
                food.getMenuName(),
                food.getCalorie(),
                food.getProtein(),
                food.getFat(),
                food.getCarbohydrate()
        );
    }
}
