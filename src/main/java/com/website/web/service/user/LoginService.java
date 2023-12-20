package com.website.web.service.user;

import com.website.domain.user.User;
import com.website.repository.user.UserRepository;
import com.website.web.dto.request.user.JoinFormRequest;
import com.website.web.dto.request.user.LoginFormRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

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

        User findNormalUser = userRepository.fail();

        if (findNormalUser == null) {
            return null;
        }

        return findNormalUser;
    }


}
