package com.website.exception;

public class ResourceNotFoundException extends CommonException {

    public ResourceNotFoundException(Throwable ex) {
        super(ex);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
