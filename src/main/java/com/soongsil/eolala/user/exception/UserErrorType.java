package com.soongsil.eolala.user.exception;

import com.soongsil.eolala.global.support.error.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorType implements ErrorType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    ALREADY_ONBOARDED(HttpStatus.BAD_REQUEST, "이미 온보딩이 존재하는 유저입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
