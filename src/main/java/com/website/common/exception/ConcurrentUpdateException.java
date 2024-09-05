package com.website.common.exception;

public class ConcurrentUpdateException extends CommonException{
    public ConcurrentUpdateException(Throwable ex) {
        super(ex);
    }
    public ConcurrentUpdateException(String serverMessage) {
        super(ErrorCode.CONFLICT, serverMessage);
    }

    public ConcurrentUpdateException(String serverMessage, Throwable cause) {
        super(ErrorCode.CONFLICT, serverMessage, cause);
    }

    public ConcurrentUpdateException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public ConcurrentUpdateException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
