package com.website.web.controller.api.login;

import com.website.domain.user.User;
import com.website.domain.user.constance.UserConst;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.request.TestDTO;
import com.website.web.dto.request.user.JoinFormRequest;
import com.website.web.dto.request.user.LoginFormRequest;
import com.website.web.dto.common.ApiResponseBody;
import com.website.web.service.common.BindingResultUtils;
import com.website.web.service.user.LoginService;
import com.website.web.service.user.LoginFormValidatorEx;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final LoginFormValidatorEx loginFormValidatorEx;
    private final BindingResultUtils bindingResultUtils;

    @PostMapping(value = "/join/user")
    public ResponseEntity joinUser(@Validated @RequestBody JoinFormRequest joinFormRequest, BindingResult bindingResult) {

        //DB 조회 전 형식 validate
        loginFormValidatorEx.validateEachPasswordEquals(joinFormRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            ApiResponseBody body = ApiResponseBody.builder()
                    .data(null)
                    .apiError(new ApiError(bindingResult)).build();
            return ResponseEntity.badRequest().body(body);
        }

        //DB 조회 후 중복 방지
        loginFormValidatorEx.validateDuplicatedEmail(joinFormRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            ApiResponseBody body = ApiResponseBody.builder()
                    .data(null)
                    .apiError(new ApiError(bindingResult)).build();
            return ResponseEntity.badRequest().body(body);
        }

        //정상 처리
        User joinedUser = loginService.join(joinFormRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/login/user")
    public ResponseEntity loginUser(@Validated @RequestBody LoginFormRequest loginFormRequest, BindingResult bindingResult, HttpServletRequest request) {

        //Bean validation
        if (bindingResult.hasErrors()) {

            ApiResponseBody apiBody = ApiResponseBody.builder()
                    .data(null)
                    .apiError(new ApiError(bindingResult))
                    .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build();

            return ResponseEntity.badRequest().body(apiBody);
        }

        //이미 세션에 유저가 있을 경우
        HttpSession prevSession = request.getSession(false);
        if (prevSession != null && prevSession.getAttribute(UserConst.USER_ID) != null) {
            return ResponseEntity.badRequest().build();
        }

        //입력한 정보대로 유저가 있는 지
        User user = loginService.findUser(loginFormRequest);
        if (user == null) {
            bindingResultUtils.addObjectMessagesTo(bindingResult, "Nodata.user");
            ApiError apiError = new ApiError(bindingResult);
            ApiResponseBody apiBody = ApiResponseBody.builder().data(null).apiError(apiError).message("No user").build();

            return ResponseEntity.badRequest().body(apiBody);
        }

        HttpSession session = request.getSession();
        session.setAttribute(UserConst.USER_ID, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            ApiResponseBody body = ApiResponseBody.builder().message("session already did not exist").build();
            return ResponseEntity.ok().body(body);
        }

        session.invalidate();
        ApiResponseBody body = ApiResponseBody.builder().message("session invalidated").build();
        return ResponseEntity.ok().body(body);
    }



}
