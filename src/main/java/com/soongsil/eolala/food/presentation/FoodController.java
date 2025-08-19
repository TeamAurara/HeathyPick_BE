package com.soongsil.eolala.food.presentation;

import com.soongsil.eolala.food.application.FoodService;
import com.soongsil.eolala.food.dto.response.FoodResponse;
import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/search")
    public ApiResponse<FoodResponse> search(String query) {
        return ApiResponse.success(foodService.searchFood(query));
    }
}
