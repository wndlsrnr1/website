package com.website.common.exception;

public class AlreadyExistsException extends CommonException {

    public AlreadyExistsException(Throwable ex) {
        super(ex);
    }

    public AlreadyExistsException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public AlreadyExistsException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
