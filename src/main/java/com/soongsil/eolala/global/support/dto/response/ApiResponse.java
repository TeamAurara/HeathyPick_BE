package com.soongsil.eolala.global.support.dto.response;

import com.soongsil.eolala.global.support.error.ErrorType;



public record ApiResponse<T>(
        boolean isSuccess,
        String code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", null);
    }

    public static <T> ApiResponse<T> error(ErrorType errorType) {
        return new ApiResponse<>(false, errorType.name(), errorType.getMessage(), null);
    }
}
