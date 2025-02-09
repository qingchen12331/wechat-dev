package com.qingchen.controller;


import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.tasks.SMSTask;
import com.qingchen.utils.IPUtil;
import com.qingchen.utils.MyInfo;
import com.qingchen.utils.RedisOperator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.qingchen.base.BaseInfoProperties.MOBILE_SMSCODE;

@RestController
@RequestMapping("/passport")
@Slf4j
public class PassportController {
    @Resource
    private SMSTask smsTask;
    @Autowired
    private RedisOperator redis;

    @GetMapping("getSMSCode")
    public GraceJSONResult getSMSCode(String mobile,HttpServletRequest request) throws Exception {
        if(StringUtils.isBlank(mobile)){
            return GraceJSONResult.error();
        }
        //获得用户的手机号/ip
        String userIp=IPUtil.getRequestIp(request);
        //限制该用户的手机号/ip在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE+":"+userIp,mobile);
        String code =(int)((Math.random()*9+1)*100000)+"";

        smsTask.sendSMSInTask(MyInfo.getMobile(),code);
        //把验证码存入redis中,用于后续的注册和登录
        redis.set(MOBILE_SMSCODE+":"+mobile,code,30*60);

        return GraceJSONResult.ok();

    }


}
