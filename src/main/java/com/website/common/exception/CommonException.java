package com.website.common.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class CommonException extends RuntimeException{
    private ErrorCode errorCode;
    private String serverMessage;

    public CommonException(Throwable ex) {
        super(ex);
    }

    public CommonException(ErrorCode errorCode, String serverMessage) {
        super(serverMessage);
        this.serverMessage = serverMessage;
        this.errorCode = errorCode;
    }

    public CommonException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(serverMessage, cause);
        this.errorCode = errorCode;
    }
}
