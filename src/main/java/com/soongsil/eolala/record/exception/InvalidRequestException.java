package com.soongsil.eolala.record.exception;

import com.soongsil.eolala.global.support.error.GlobalException;

public class InvalidRequestException extends GlobalException {
    public InvalidRequestException(RecordsErrorType errorType) {
        super(errorType);
    }
}
