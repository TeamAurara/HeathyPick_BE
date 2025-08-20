package com.soongsil.eolala.record.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DailyRecordResponse(
        @Schema(description = "날짜", example = "2023-10-01")
        LocalDate date,

        @Schema(description = "총 아침 섭취 칼로리", example = "500.0")
        Double totalMorningCalorie,

        @Schema(description = "총 점심 섭취 칼로리", example = "700.0")
        Double totalLunchCalorie,

        @Schema(description = "총 저녁 섭취 칼로리", example = "600.0")
        Double totalDinnerCalorie,

        @Schema(description = "총 물 섭취량 (리터 단위)", example = "2.0")
        Double totalWater
) {
    public static DailyRecordResponse of(LocalDate date, Double totalMorningCalorie, Double totalLunchCalorie, Double totalDinnerCalorie, Double totalWater) {
        return DailyRecordResponse.builder()
                .date(date)
                .totalMorningCalorie(totalMorningCalorie)
                .totalLunchCalorie(totalLunchCalorie)
                .totalDinnerCalorie(totalDinnerCalorie)
                .totalWater(totalWater)
                .build();
    }
}
