package com.website.exception;

public class ServerException extends CommonException{
    public ServerException(Throwable ex) {
        super(ex);
    }

    public ServerException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public ServerException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
