package com.soongsil.eolala.auth.exception;

import com.soongsil.eolala.global.support.error.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorType implements ErrorType {

    REFRESH_TOKEN_ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RefreshToken 암호화에 실패하였습니다."),
    REFRESH_TOKEN_DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RefreshToken 복호화에 실패하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
} 