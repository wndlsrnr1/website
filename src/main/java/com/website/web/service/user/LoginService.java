package com.website.web.service.user;

import com.website.domain.user.User;
import com.website.repository.user.UserRepository;
import com.website.web.dto.request.user.JoinFormRequest;
import com.website.web.dto.request.user.LoginFormRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.website.web.constance.Regexes;
import com.website.web.dto.common.ApiError;
import com.website.web.dto.common.ApiResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final LoginFormValidatorEx loginFormValidatorEx;

    public User join(JoinFormRequest joinFormRequest) {

        User user = User.builder().name(joinFormRequest.getName())
                .email(joinFormRequest.getEmail())
                .address(joinFormRequest.getAddress())
                .password(joinFormRequest.getPassword())
                .build();
        userRepository.saveUser(user);
        return user;
    }



    //public User findUser(LoginFormRequest loginFormRequest) {
    //    String password = loginFormRequest.getPassword();
    //    String email = loginFormRequest.getEmail();
    //
    //    User findNormalUser = userJpaRepository.findNormalUserByEmailPassword(email, password);
    //
    //    if (findNormalUser == null) {
    //        return null;
    //    }
    //
    //    return findNormalUser;
    //}
    public User findUser(LoginFormRequest loginFormRequest) {
        String password = loginFormRequest.getPassword();
        String email = loginFormRequest.getEmail();

        User findNormalUser = userRepository.findNormalUserByEmailPassword(email, password);

        if (findNormalUser == null) {
            return null;
        }
        return findNormalUser;
    }

    //public User findUser(LoginFormRequest loginFormRequest) {
    //    String password = loginFormRequest.getPassword();
    //    String email = loginFormRequest.getEmail();
    //
    //    User findNormalUser = userJpaRepository.findNormalUserByEmailPassword(email, password);
    //
    //    if (findNormalUser == null) {
    //        return null;
    //    }
    //
    //    return findNormalUser;
    //}




    public User findUserByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }


    public ResponseEntity validateEmail(String email) {
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean matches = email.matches(Regexes.EMAIL_PATTERN);
        log.info("matches = {}", matches);
        //email형식이 맞지 않으면 badReqeust Return
        if (!matches) {
            return ResponseEntity.badRequest().build();
        }

        //User가 있으면 문제 있음
        User findUser = userRepository.findUserByEmail(email);
        if (findUser != null) {
            return ResponseEntity.badRequest().build();
        }

        //확실히 하기위해서 조건문
        if (matches && findUser == null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    public ResponseEntity joinUser(JoinFormRequest joinFormRequest, BindingResult bindingResult) {

        log.info("joinFormRequest = {}", joinFormRequest);

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
        User joinedUser = this.join(joinFormRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
