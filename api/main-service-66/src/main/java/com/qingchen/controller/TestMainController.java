package com.qingchen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class TestMainController {
    @RequestMapping("test")
    public Object test()
    {
        return "main";
    }
}
