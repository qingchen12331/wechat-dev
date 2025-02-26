package com.qingchen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient//开启服务的注册和发现功能
@MapperScan(basePackages = "com.qingchen.mapper")
@EnableFeignClients("com.qingchen.api.feign")
public class Application {
    public static void main(String[]args){
        SpringApplication.run(Application.class,args);
    }

}
