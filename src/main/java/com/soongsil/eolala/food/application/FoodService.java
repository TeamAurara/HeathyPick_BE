package com.soongsil.eolala.food.application;

import com.soongsil.eolala.food.domain.Food;
import com.soongsil.eolala.food.persistence.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Optional<Food> getFood(Long foodId) {
        return foodRepository.findById(foodId);
    }
}
