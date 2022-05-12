package com.practice.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/app/oauth2/token")
    public String testInterface() {
        return "success";
    }


}
