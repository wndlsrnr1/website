package com.website.controller.api.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JustController {

    @GetMapping(value = "/throw", consumes = "text/html")
    public String throwException() {
        throw new RuntimeException();
        //return "error/404";
    }

}
