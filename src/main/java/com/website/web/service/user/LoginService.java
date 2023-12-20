package com.website.web.service.user;

import com.website.domain.user.User;
import com.website.repository.user.UserJpaRepository;
import com.website.repository.user.UserRepository;
import com.website.web.dto.user.JoinForm;
import com.website.web.dto.user.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserJpaRepository userJpaRepository;

    public User join(JoinForm joinForm) {

        User user = User.builder().name(joinForm.getName())
                .email(joinForm.getEmail())
                .address(joinForm.getAddress())
                .password(joinForm.getPassword())
                .build();
        userJpaRepository.saveUser(user);

        return user;
    }

    public User findUser(LoginForm loginForm) {
        String password = loginForm.getPassword();
        String email = loginForm.getEmail();

        User findNormalUser = userJpaRepository.findNormalUserByEmailPassword(email, password);

        if (findNormalUser == null) {
            return null;
        }

        return findNormalUser;
    }
}
