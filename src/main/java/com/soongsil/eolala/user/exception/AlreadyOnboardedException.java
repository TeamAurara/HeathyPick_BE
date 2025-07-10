package com.soongsil.eolala.user.exception;

import com.soongsil.eolala.global.support.error.GlobalException;
import lombok.Getter;

@Getter
public class AlreadyOnboardedException extends GlobalException {

    public AlreadyOnboardedException(UserErrorType errorType) {
        super(errorType);
    }
}
