package com.qingchen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient//开启服务的注册和发现功能
@MapperScan(basePackages = "com.qingchen.mapper")
public class Application {
    public static void main(String[]args){
        SpringApplication.run(Application.class,args);
    }

}
