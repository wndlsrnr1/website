package com.website.common.exception;

public class OrderAlreadyExistsException extends CommonException {
    public OrderAlreadyExistsException(Throwable ex) {
        super(ex);
    }

    public OrderAlreadyExistsException(String serverMessage) {
        super(ErrorCode.CONFLICT, serverMessage);
    }

    public OrderAlreadyExistsException(String serverMessage, Throwable cause) {
        super(ErrorCode.CONFLICT, serverMessage, cause);
    }

    public OrderAlreadyExistsException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public OrderAlreadyExistsException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
