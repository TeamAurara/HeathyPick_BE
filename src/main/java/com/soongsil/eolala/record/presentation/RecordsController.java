package com.soongsil.eolala.record.presentation;

import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import com.soongsil.eolala.record.application.RecordsService;
import com.soongsil.eolala.record.dto.request.RecordsCustomFoodRequest;
import com.soongsil.eolala.record.dto.request.RecordsFoodRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @Operation(summary = "사용자 하루 섭취 기록 조회 api", description = "사용자의 하루 섭취 기록을 조회합니다.")
    @GetMapping("/{userId}/daily")
    public ApiResponse<?> getDailyRecords(
            @PathVariable Long userId,

            @Schema(description = "섭취 조회할 날짜", example = "2025-01-01")
            @RequestParam(required = true) LocalDate date
    ) {
        return ApiResponse.success(recordsService.getDailyRecords(userId, date));
    }
}
