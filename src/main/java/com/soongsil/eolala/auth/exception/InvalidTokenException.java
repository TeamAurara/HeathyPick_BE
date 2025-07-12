package com.soongsil.eolala.auth.exception;

import com.soongsil.eolala.global.support.error.GlobalException;

public class InvalidTokenException extends GlobalException {
    
    public InvalidTokenException() {
        super(AuthErrorType.INVALID_TOKEN);
    }
    
    public InvalidTokenException(Throwable cause) {
        super(AuthErrorType.INVALID_TOKEN, cause);
    }
} 