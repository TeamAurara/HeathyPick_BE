package com.soongsil.eolala.record.presentation;

import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.record.RecordsService;
import com.soongsil.eolala.record.dto.request.RecordsCustomFoodRequest;
import com.soongsil.eolala.record.dto.request.RecordsFoodRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordsController {

    private final RecordsService recordsService;

    @Operation(summary = "검색한 섭취 음식 저장 api", description = "검색하여 얻은 사용자의 섭취 음식을 저장합니다.")
    @PostMapping("/{userId}/food")
    public ApiResponse<?> saveFoodRecord(
            @PathVariable Long userId,
            @Valid @RequestBody RecordsFoodRequest recordsFoodRequest
    ) {
        recordsService.saveFoodRecord(userId, recordsFoodRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "사용자 직접 입력한 섭취 음식 저장 api", description = "사용자가 직접 입력한 섭취 음식을 저장합니다.")
    @PostMapping("/{userId}/custom-food")
    public ApiResponse<?> saveCustomFoodRecord(
            @PathVariable Long userId,
            @Valid @RequestBody RecordsCustomFoodRequest recordsCustomFoodRequest
    ) {
        recordsService.saveCustomFoodRecord(userId, recordsCustomFoodRequest);
        return ApiResponse.success();
    }
}
