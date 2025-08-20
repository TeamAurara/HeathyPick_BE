package com.soongsil.eolala.food.application;

import com.soongsil.eolala.food.domain.Food;
import com.soongsil.eolala.food.dto.response.FoodResponse;
import com.soongsil.eolala.food.persistence.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Optional<Food> getFood(Long foodId) {
        return foodRepository.findById(foodId);
    }

    public FoodResponse searchFood(String menuName) {
        List<FoodResponse> searchFoodList = foodRepository.findAllByMenuName(menuName)
                .stream()
                .map(FoodResponse::from)
                .toList();

        return searchFoodList.getFirst();
    }
}
