package com.shopu.exception;

import org.springframework.context.ApplicationContextException;

public class ApplicationException extends ApplicationContextException {
    public ApplicationException(String message) {
        super(message);
    }
}
