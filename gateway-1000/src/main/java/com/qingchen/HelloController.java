package com.qingchen;

import com.qingchen.base.BaseInfoProperties;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/g")
public class HelloController  {
 @Autowired
 private RedisOperator redis;
    @GetMapping("hello")
    public Object hello(){

        return "hello gateway";
    }
    @GetMapping("setredis")
    public Object setRedis(){
        redis.set("test","test");
        return GraceJSONResult.ok();
    }
    @GetMapping("getredis")
    public Object getRedis(){
        String test = redis.get("test");
        return GraceJSONResult.ok(test);
    }
}
