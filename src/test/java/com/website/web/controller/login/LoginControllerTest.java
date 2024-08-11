package com.website.web.controller.login;

import com.website.repository.model.user.User;
import com.website.repository.user.CustomUserRepository;
//import com.website.repository.user.UserRepository2;
import com.website.repository.user.UserRepository;
import com.website.controller.api.model.request.user.JoinFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class LoginControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserRepository customUserRepository;

    @BeforeEach
    void saveData() {
        JoinFormRequest joinFormRequest = JoinFormRequest.builder()
                .email("wndlsrnr1@gmail.com")
                .password("1234")
                .name("주인국")
                .address("myHome")
                .build();

        User user = User.builder().name(joinFormRequest.getName())
                .email(joinFormRequest.getEmail())
                .address(joinFormRequest.getAddress()).build();
        userRepository.save(user);

    }

    @Test
    void joinTest() {

        JoinFormRequest joinFormRequest = JoinFormRequest.builder()
                .email("wndlsrnr1@gmail.com")
                .password("1234")
                .name("주인국")
                .address("myHome")
                .build();

        User user = User.builder().name(joinFormRequest.getName())
                .email(joinFormRequest.getEmail())
                .address(joinFormRequest.getAddress())
                .password(joinFormRequest.getPassword())
                .build();
        userRepository.save(user);

        userRepository.flush();

    }

    @Test
    void findAnyTest() {
        List<User> all = userRepository.findAll();
        log.info("all = {}", all);
    }

    @Test
    void loginTest() {
        User user = userRepository.findAll().get(0);
        String email = user.getEmail();
        String password = user.getPassword();
        User findNormalUser = customUserRepository.findNormalUserByEmailPassword(email, password);

        log.info("findNormalUser = {}", findNormalUser);
    }

}