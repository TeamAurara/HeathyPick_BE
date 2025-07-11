package com.soongsil.eolala.user.exception;

import com.soongsil.eolala.global.support.error.GlobalException;
import lombok.Getter;

@Getter
public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(UserErrorType errorType) {
        super(errorType);
    }
}
