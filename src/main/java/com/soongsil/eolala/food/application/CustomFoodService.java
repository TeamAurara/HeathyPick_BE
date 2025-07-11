package com.soongsil.eolala.food.application;


import com.soongsil.eolala.food.domain.CustomFood;
import com.soongsil.eolala.food.persistence.CustomFoodRepository;
import com.soongsil.eolala.record.dto.request.RecordsCustomFoodRequest;
import com.soongsil.eolala.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomFoodService {

    private final CustomFoodRepository customFoodRepository;

    @Transactional
    public CustomFood saveCustomFoodRecord(User user, RecordsCustomFoodRequest recordsCustomFoodRequest) {
        CustomFood food = CustomFood.builder()
                .user(user)
                .menuName(recordsCustomFoodRequest.menuName())
                .calorie(recordsCustomFoodRequest.calorie())
                .protein(recordsCustomFoodRequest.protein())
                .carbohydrate(recordsCustomFoodRequest.carbohydrate())
                .fat(recordsCustomFoodRequest.fat())
                .build();

        customFoodRepository.save(food);
        return food;
    }

    public Optional<CustomFood> getCustomFood(Long customFoodId) {
        return customFoodRepository.findById(customFoodId);
    }
}
