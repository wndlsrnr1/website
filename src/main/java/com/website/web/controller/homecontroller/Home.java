package com.website.web.controller.homecontroller;

import com.website.domain.User;
import com.website.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Home {

    private final UserRepository userRepository;

    @GetMapping("/check/dto")
    public String checkDto() {
        User iam = new User("IAM");
        userRepository.save(iam);
        userRepository.flush();
        List<User> all = userRepository.findAll();
        User user = all.get(0);
        String name = user.getName();
        return name;
    }

    @RequestMapping("/users")
    public List<User> getUsersAll() {
        log.info("dsafsadfasdfasdfasdfsadf");
        List<User> all = userRepository.findAll();
        log.info("users = {}", all);
        return all;
    }
    //npm install --save bootstrap@5.1 react-cookie@4.1.1 react-router-dom@5.3.0 reactstrap@8.10.0
}
