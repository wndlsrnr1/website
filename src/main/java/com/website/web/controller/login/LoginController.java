package com.website.web.controller.login;

import com.website.domain.user.User;
import com.website.domain.user.constance.UserConst;
import com.website.web.dto.user.JoinForm;
import com.website.web.dto.user.LoginForm;
import com.website.web.service.user.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/join/user")
    public String joinUser(@RequestBody JoinForm joinForm) {

        User joinedUser = loginService.join(joinForm);

        log.info("joinUser = {}", joinedUser);

        return "good";
    }

    @PostMapping("/login/user")
    public String loginUser(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        HttpSession prevSession = request.getSession(false);
        if (prevSession != null && prevSession.getAttribute(UserConst.USER_ID) != null) {
            return "login-fail";
        }

        User user = loginService.findUser(loginForm);

        if (user == null) {
            return "login-fail";
        }

        HttpSession session = request.getSession();
        session.setAttribute(UserConst.USER_ID, user.getId());

        log.info("loginUser = {}", user);

        return "ok";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "just-return";
        }

        session.invalidate();
        return "session is invalidated";
    }

}
