package com.website.web.service.user;

import com.website.domain.user.User;
import com.website.repository.user.UserRepository;
import com.website.web.dto.request.user.JoinFormRequest;
import com.website.web.service.common.BindingResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class LoginFormValidatorEx {

    private final UserRepository userRepository;
    private final BindingResultUtils bindingResultUtils;

    public void validateEachPasswordEquals(JoinFormRequest joinFormRequest, BindingResult bindingResult) {

        String password = joinFormRequest.getPassword();
        String password2 = joinFormRequest.getPassword2();

        if (!password.equals(password2)) {
            bindingResultUtils.addFieldMessagesTo(bindingResult, "password", "NotMatches.password");
            bindingResultUtils.addObjectMessagesTo(bindingResult, "NotMatches");
        }
    }

    public void validateDuplicatedEmail(JoinFormRequest joinFormRequest, BindingResult bindingResult) {
        String email = joinFormRequest.getEmail();
        User userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail != null) {
            bindingResultUtils.addFieldMessagesTo(bindingResult, "email", "AlreadyExist.email");
            bindingResultUtils.addObjectMessagesTo(bindingResult, "AlreadyExist.email");
        }
    }



}
