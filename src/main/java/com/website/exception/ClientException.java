package com.website.exception;

public class ClientException extends CommonException{

    public ClientException(Throwable ex) {
        super(ex);
    }

    public ClientException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public ClientException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }

}
