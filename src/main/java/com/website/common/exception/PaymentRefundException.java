package com.website.common.exception;

public class PaymentRefundException extends CommonException {

    public PaymentRefundException(Throwable ex) {
        super(ex);
    }

    public PaymentRefundException(String serverMessage) {
        super(ErrorCode.BAD_REQUEST, serverMessage);
    }

    public PaymentRefundException(String serverMessage, Throwable cause) {
        super(ErrorCode.BAD_REQUEST, serverMessage, cause);
    }

    public PaymentRefundException(ErrorCode errorCode, String serverMessage) {
        super(errorCode, serverMessage);
    }

    public PaymentRefundException(ErrorCode errorCode, String serverMessage, Throwable cause) {
        super(errorCode, serverMessage, cause);
    }
}
