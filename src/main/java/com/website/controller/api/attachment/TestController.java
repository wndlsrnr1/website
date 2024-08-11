package com.website.controller.api.attachment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.function.ServerRequest;

@Controller
public class TestController {
    @GetMapping(value = "/attachment/save/one")
    public String showTemplate() {
        return "sendAttachmentRequest";
    }
}
