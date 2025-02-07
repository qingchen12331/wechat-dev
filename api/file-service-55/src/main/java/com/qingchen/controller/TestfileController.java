package com.qingchen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/b")
public class TestfileController {
    @GetMapping("test")
    public Object test(){
        return "test~";
    }
}
