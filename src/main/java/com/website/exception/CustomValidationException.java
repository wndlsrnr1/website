package com.website.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Getter
public class CustomValidationException extends CommonException {
    private final Map<String, String> errorMessages;

    public CustomValidationException(Throwable ex, Map<String, String> errorMessages) {
        super(ex);
        this.errorMessages = errorMessages;
    }

    public CustomValidationException(ErrorCode errorCode, String serverMessage, Map<String, String> errorMessages) {
        super(errorCode, serverMessage);
        this.errorMessages = errorMessages;
    }

    public CustomValidationException(ErrorCode errorCode, String serverMessage, Throwable cause, Map<String, String> errorMessages) {
        super(errorCode, serverMessage, cause);
        this.errorMessages = errorMessages;
    }
}
