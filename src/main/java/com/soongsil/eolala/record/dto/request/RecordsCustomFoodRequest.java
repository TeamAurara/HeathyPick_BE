package com.soongsil.eolala.record.dto.request;

import com.soongsil.eolala.record.domain.type.IntakeTimeType;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecordsCustomFoodRequest(
        @Schema(description = "메뉴명", example = "황금올리브치킨", requiredMode = Schema.RequiredMode.REQUIRED)
        String menuName,

        @Schema(description = "칼로리", example = "500.0", format = "double", nullable = true)
        Double calorie,

        @Schema(description = "탄수화물", example = "50.0", format = "double", nullable = true)
        Double carbohydrate,

        @Schema(description = "단백질", example = "30.0", format = "double", nullable = true)
        Double protein,

        @Schema(description = "지방", example = "20.0", format = "double", nullable = true)
        Double fat,

        @Schema(description = "섭취 시간", example = "BREAKFAST", requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"BREAKFAST", "LUNCH", "DINNER"})
        IntakeTimeType intakeTimeType
) {
}
