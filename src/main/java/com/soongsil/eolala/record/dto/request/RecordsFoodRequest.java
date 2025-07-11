package com.soongsil.eolala.record.dto.request;

import com.soongsil.eolala.record.domain.type.IntakeTimeType;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecordsFoodRequest(
        @Schema(description = "식품 영양 DB에 있는 음식 ID", example = "1", nullable = true)
        Long foodId,

        @Schema(description = "사용자가 직접 입력한 음식 ID", example = "1", nullable = true)
        Long customFoodId,

        @Schema(description = "섭취 시간", example = "BREAKFAST", requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"BREAKFAST", "LUNCH", "DINNER"})
        IntakeTimeType intakeTimeType
) {
}
