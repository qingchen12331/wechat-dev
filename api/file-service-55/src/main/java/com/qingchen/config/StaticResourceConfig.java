package com.qingchen.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class StaticResourceConfig extends WebMvcConfigurationSupport {
    /**
     * 添加静态资源映射路径
     * @param registry
     */
    /**
     * addResourceHandler:指的是对外暴露的访问映射路径
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:C:\\Users\\27175\\Desktop\\git_test\\temp");
        super.addResourceHandlers(registry);
    }
}
