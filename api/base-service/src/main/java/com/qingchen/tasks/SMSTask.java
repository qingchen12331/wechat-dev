package com.qingchen.tasks;

import com.qingchen.utils.SMSUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SMSTask {
    @Resource
    private SMSUtils smsUtils;
    @Async
    public void sendSMSInTask(String mobile,String code) throws Exception{
//        smsUtils.sendSMS(mobile,code);
        log.info("异步发送的验证码为:{}",code);
    }
}
