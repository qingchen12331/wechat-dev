package com.qingchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class )
@EnableDiscoveryClient//开启服务的注册和发现功能
@EnableFeignClients("com.qingchen.api.feign")
public class Application {
    public static void main(String[]args){
        SpringApplication.run(Application.class,args);
    }
}
