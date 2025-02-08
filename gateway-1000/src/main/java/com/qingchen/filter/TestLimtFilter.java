package com.qingchen.filter;

import com.qingchen.base.BaseInfoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TestLimtFilter extends BaseInfoProperties implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("过滤器的优先级为0");
        return chain.filter(exchange);
    }

    /**
     * 过滤器的顺序,数字越小优先级越大
     * @return
     */
    @Override
    public int getOrder() {

        return 0;
    }
}
