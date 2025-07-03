package com.soongsil.eolala.global.support.dto.response;

import com.soongsil.eolala.global.support.error.ErrorType;

import java.util.Collections;
import java.util.Map;

public record ApiResponse<T>(
        boolean isSuccess,
        String code,
        String message,
        T data
) {
    private static final Map<String, Object> EMPTY_DATA = Collections.emptyMap();

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    public static ApiResponse<Object> success() {
        return new ApiResponse<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", EMPTY_DATA);
    }

    public static ApiResponse<Object> error(ErrorType errorType) {
        return new ApiResponse<>(false, errorType.name(), errorType.getMessage(), EMPTY_DATA);
    }
}
