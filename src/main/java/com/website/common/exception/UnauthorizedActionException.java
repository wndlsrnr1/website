package com.website.common.exception;

public class UnauthorizedActionException extends CommonException {
    public UnauthorizedActionException(Throwable ex) {
        super(ex);
    }
    public UnauthorizedActionException(String serverMessage) {
        super(ErrorCode.UNAUTHORIZED, serverMessage);
    }

    public UnauthorizedActionException(String serverMessage, Throwable cause) {
        super(ErrorCode.UNAUTHORIZED, serverMessage, cause);
    }

    public UnauthorizedActionException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public UnauthorizedActionException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
