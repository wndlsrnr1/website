package com.website.common.exception;

public class PaymentFailedException extends CommonException {
    public PaymentFailedException(Throwable ex) {
        super(ex);
    }

    public PaymentFailedException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public PaymentFailedException(String serverMessage) {
        super(ErrorCode.BAD_REQUEST, serverMessage);
    }

    public PaymentFailedException(String serverMessage, Throwable cause) {
        super(ErrorCode.BAD_REQUEST, serverMessage, cause);
    }

    public PaymentFailedException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
