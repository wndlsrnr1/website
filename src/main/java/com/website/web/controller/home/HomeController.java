package com.website.web.controller.home;

import com.website.domain.user.User;
import com.website.repository.user.UserJpaRepository;
import com.website.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final UserJpaRepository userJpaRepository;

}
