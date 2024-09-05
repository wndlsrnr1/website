package com.website.common.exception;

public class OutOfStockException extends CommonException {
    public OutOfStockException(Throwable ex) {
        super(ex);
    }

    public OutOfStockException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public OutOfStockException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
