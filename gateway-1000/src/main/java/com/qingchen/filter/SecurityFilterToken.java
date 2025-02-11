package com.qingchen.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.grace.result.ResponseStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RefreshScope
public class SecurityFilterToken extends BaseInfoProperties implements GlobalFilter, Ordered {
    @Resource
    private ExcludeUrlProperties excludeUrlProperties;
    private AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获得当前用户请求的路径URL
        String url=exchange.getRequest().getURI().getPath();
        log.info("SecurityFilterToken url={}",url);
        //2.获得所有的徐永涛排除校验的url list
        List<String> excludeList=excludeUrlProperties.getUrls();
        //3.1校验并且排除excludeList
        if(excludeList!=null&&!excludeList.isEmpty()){
            for(String excludeUrl:excludeList){
                if(antPathMatcher.matchStart(excludeUrl,url)){
                    //如果匹配到,则直接放行,表示当前的URL是不需要被拦截校验的
                    return chain.filter(exchange);
                }
            }
        }
        //3.2排除静态资源服务static
        String fileStart = excludeUrlProperties.getFileStart();
        if(StringUtils.isNotBlank(fileStart)){
            boolean matchFileStart=antPathMatcher.matchStart(fileStart,url);
            if(matchFileStart){
                return chain.filter(exchange);
            }
        }
        //4.代码到达此处,表示请求被拦截,需要进行校验
        log.info("当前请求路径[{}]被拦截...",url);
        //5.判断header中是否有Token,对用户请求进行判断拦截
        HttpHeaders headers=exchange.getRequest().getHeaders();
        String userId=headers.getFirst(HEADER_USER_ID);
        String userToken=headers.getFirst(HEADER_USER_TOKEN);
        log.info("userId={}",userId);
        log.info("userToken={}",userToken);
        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){
            log.info("userId与userToken都不为空");
            String redisToken=redis.get(REDIS_USER_TOKEN+":"+userId);
            if(redisToken.equalsIgnoreCase(userToken)){
                log.info("放行成功");
                //匹配则放行
                return chain.filter(exchange);
            }
        }


        return RenderErrorUtils.display(exchange, ResponseStatusEnum.UN_LOGIN);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
