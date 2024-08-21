package com.website.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    FORBIDDEN(HttpStatus.FORBIDDEN, "인증 정보가 잘못 되었습니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 사용자 요청"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 규칙을 확인하세요"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "작업중 에러가 발생했습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String clientMessage;

}
