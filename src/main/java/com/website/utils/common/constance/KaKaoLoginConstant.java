package com.website.utils.common.constance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum KaKaoLoginConstant {
    GRANT_TYPE("authorization_code"),
    CLIENT_ID("b678b03b04bcdee81052dad6e436c06c"),
    REGISTER_REDIRECT_URI("http://localhost:3000/auth/user/register/kakao"),
    //LOGOUT_REDIRECT_URI("http://localhost:3000/auth/user/logout/kakao"),
    LOGIN_REDIRECT_URI("http://localhost:3000/auth/user/login/kakao"),
    DELETE_REDIRECT_URI("http://localhost:3000/auth/user/delete/kakao"),
    AUTH_URL("https://kauth.kakao.com/oauth/token"),
    ;
    private final String value;
}
