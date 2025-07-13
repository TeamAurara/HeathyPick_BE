package com.soongsil.eolala.auth.exception;

import com.soongsil.eolala.global.support.error.GlobalException;

public class ExpiredTokenException extends GlobalException {
    
    public ExpiredTokenException() {
        super(AuthErrorType.EXPIRED_TOKEN);
    }
    
    public ExpiredTokenException(Throwable cause) {
        super(AuthErrorType.EXPIRED_TOKEN, cause);
    }
} 