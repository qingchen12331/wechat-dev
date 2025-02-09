package com.qingchen.controller;


import com.qingchen.tasks.SMSTask;
import com.qingchen.utils.MyInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class HelloController {
    @Resource
    private SMSTask smsTask;
    @GetMapping("hello")
    public Object hello(){
        return "Hello World~";
    }
    @GetMapping("smsTask")
    public Object smsTask() throws Exception {
        smsTask.sendSMSInTask(MyInfo.getMobile(),"8111");
        return "Send Ok";
    }


}
