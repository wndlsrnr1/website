package com.website.common.exception;

public class UnauthorizedActionException extends CommonException {
    public UnauthorizedActionException(Throwable ex) {
        super(ex);
    }

    public UnauthorizedActionException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public UnauthorizedActionException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
