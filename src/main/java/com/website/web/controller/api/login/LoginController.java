package com.website.web.controller.api.login;

import com.website.domain.user.User;
import com.website.domain.user.constance.UserConst;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.request.TestDTO;
import com.website.web.dto.request.user.JoinFormRequest;
import com.website.web.dto.request.user.LoginFormRequest;
import com.website.web.dto.common.ApiResponseBody;
//import com.website.web.dto.response.UserLoginResponse;
import com.website.web.dto.response.user.LoginResponse;
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

    @GetMapping("/login/auth")
    public ResponseEntity authLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(UserConst.USER_ID) != null) {
            Long userId = (Long) session.getAttribute(UserConst.USER_ID);
            User findUser = loginService.findUserByUserId(userId);
            if (findUser.getId() == null || !findUser.getId().equals(userId)) {
                ApiResponseBody<Object> body = ApiResponseBody.builder().data(new LoginResponse(false)).build();
                return ResponseEntity.ok().body(body);
            }
            ApiResponseBody<Object> body = ApiResponseBody.builder().data(new LoginResponse(true)).build();
            return ResponseEntity.ok().body(body);
        }

        ApiResponseBody<Object> body = ApiResponseBody.builder().data(new LoginResponse(false)).build();
        return ResponseEntity.ok().body(body);
    }

    @PostMapping(value = "/user/join")
    public ResponseEntity joinUser(@Validated JoinFormRequest joinFormRequest, BindingResult bindingResult) {
        return loginService.joinUser(joinFormRequest, bindingResult);
    }

    @PostMapping(value = "/login/user")
    public ResponseEntity loginUser(@Validated LoginFormRequest loginFormRequest, BindingResult bindingResult, HttpServletRequest request) {

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
            log.info("SessionAlready");
            bindingResultUtils.addObjectMessagesTo(bindingResult, "Already.login");
            ApiResponseBody<Object> body = ApiResponseBody.builder().message("login already").data(null).apiError(new ApiError(bindingResult)).build();
            return ResponseEntity.badRequest().body(body);
        }

        //입력한 정보대로 유저가 있는 지
        User user = loginService.findUser(loginFormRequest);
        if (user == null) {
            log.info("NoData");
            bindingResultUtils.addObjectMessagesTo(bindingResult, "Nodata.user");
            ApiError apiError = new ApiError(bindingResult);
            ApiResponseBody apiBody = ApiResponseBody.builder().data(null).apiError(apiError).message("No user").build();

            return ResponseEntity.badRequest().body(apiBody);
        }

        HttpSession session = request.getSession();
        session.setAttribute(UserConst.USER_ID, user.getId());
        return ResponseEntity.ok().body(ApiResponseBody.ok());
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

    @GetMapping("/email/check")
    public ResponseEntity loginCheck(HttpServletRequest request, @RequestParam("email") String email) {
        log.info(email);
        return loginService.validateEmail(email);
    }

}
