package com.qingchen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class HelloController {
    @GetMapping("hello")
    public Object hello(){
        return "Hello World~";
    }

}
