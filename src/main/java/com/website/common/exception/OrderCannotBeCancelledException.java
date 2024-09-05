package com.website.common.exception;

public class OrderCannotBeCancelledException extends CommonException {

    public OrderCannotBeCancelledException(Throwable ex) {
        super(ex);
    }

    public OrderCannotBeCancelledException(String serverMessage) {
        super(ErrorCode.BAD_REQUEST, serverMessage);
    }

    public OrderCannotBeCancelledException(String serverMessage, Throwable cause) {
        super(ErrorCode.BAD_REQUEST, serverMessage, cause);
    }

    public OrderCannotBeCancelledException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public OrderCannotBeCancelledException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
