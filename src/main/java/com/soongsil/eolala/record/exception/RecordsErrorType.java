package com.soongsil.eolala.record.exception;

import com.soongsil.eolala.global.support.error.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecordsErrorType implements ErrorType {
    ONLY_ONE_FOOD_ID_ALLOWED(HttpStatus.BAD_REQUEST, "foodId와 customFoodId 중 하나만 입력해야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
