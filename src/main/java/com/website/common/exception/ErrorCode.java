package com.website.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 사용자 요청"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 규칙을 확인하세요"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "작업중 에러가 발생했습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "인가되지 않은 사용자"),
    PAYMENT_REQUIRED(HttpStatus.PAYMENT_REQUIRED, "결제에 실패했습니다"),
    CONFLICT(HttpStatus.CONFLICT, "중복된 요청"),
    ;

    private final HttpStatus httpStatus;
    private final String clientMessage;

}
