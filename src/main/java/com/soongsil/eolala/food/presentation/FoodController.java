package com.soongsil.eolala.food.presentation;

import com.soongsil.eolala.food.application.FoodService;
import com.soongsil.eolala.food.dto.response.FoodResponse;
import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "음식 검색 API", description = "음식 ID로 음식 정보를 조회합니다.")
    @GetMapping("/search")
    public ApiResponse<FoodResponse> search(
            @Schema(description = "검색 할 음식 이름", example = "감자탕")
            String query
    ) {
        return ApiResponse.success(foodService.searchFood(query));
    }
}
