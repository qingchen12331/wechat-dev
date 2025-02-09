package com.qingchen;

import com.qingchen.controller.interceptor.SMSInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smsInterceptor()).addPathPatterns("/passport/getSMSCode");
    }

    @Bean
    public SMSInterceptor smsInterceptor(){
            return new SMSInterceptor();
    }


}
