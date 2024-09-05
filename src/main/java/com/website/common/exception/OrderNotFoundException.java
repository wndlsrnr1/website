package com.website.common.exception;

public class OrderNotFoundException extends CommonException {
    public OrderNotFoundException(String serverMessage) {
        super(ErrorCode.BAD_REQUEST, serverMessage);
    }

    public OrderNotFoundException(String serverMessage, Throwable cause) {
        super(ErrorCode.BAD_REQUEST, serverMessage, cause);
    }

    public OrderNotFoundException(Throwable ex) {
        super(ex);
    }

    public OrderNotFoundException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public OrderNotFoundException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
