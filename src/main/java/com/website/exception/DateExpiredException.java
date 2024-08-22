package com.website.exception;

public class DateExpiredException extends CommonException {

    public DateExpiredException(Throwable ex) {
        super(ex);
    }

    public DateExpiredException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public DateExpiredException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
