package com.qingchen.filter;

import com.google.gson.Gson;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.grace.result.ResponseStatusEnum;
import com.qingchen.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RefreshScope
public class IPLimtFilter extends BaseInfoProperties implements GlobalFilter, Ordered {

    @Value("${blackIp.continueCounts}")
    private  Integer continueCounts;
    @Value("${blackIp.timeInterval}")
    private  Integer timeInterval;
    @Value("${blackIp.limitTimes}")
    private  Integer limitTimes;
    /**
     * 需求：
     * 判断某个请求的ip在20秒内的请求次数是否超过3次
     * 如果超过3次，则限制访问30秒
     * 等待30秒静默后，才能够继续恢复访问
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("IP限流过滤器的优先级为1");
//        if(1==1){
//            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
//        }
        log.info("continueCounts:{}", continueCounts);
        log.info("timeInterval:{}", timeInterval);
        log.info("limitTimes:{}", limitTimes);
        return doLimit(exchange, chain);


    }
    /**
     * 限制ip请求次数的判断
     */
    public Mono<Void> doLimit(ServerWebExchange exchange,
                              GatewayFilterChain chain) {

        // 根据request获得请求ip
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIP(request);

        // 正常的ip定义
        final String ipRedisKey = "gateway-ip:" + ip;
        // 被拦截的黑名单ip，如果在redis中存在，则表示目前被关小黑屋
        final String ipRedisLimitKey = "gateway-ip:limit:" + ip;

        // 获得当前的ip并且查询还剩下多少时间，如果时间存在（大于0），则表示当前仍然处在黑名单中
        long limitLeftTimes = redis.ttl(ipRedisLimitKey);
        if (limitLeftTimes > 0) {
            // 终止请求，返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        // 在redis中获得ip的累加次数
        long requestCounts = redis.increment(ipRedisKey, 1);
        /**
         * 判断如果是第一次进来，也就是从0开始计数，则初期访问就是1，
         * 需要设置间隔的时间，也就是连续请求的次数的间隔时间
         */
        if (requestCounts == 1) {
            redis.expire(ipRedisKey, timeInterval);
        }

        /**
         * 如果还能获得请求的正常次数，说明用户的连续请求落在限定的[timeInterval]之内
         * 一旦请求次数超过限定的连续访问次数[continueCounts]，则需要限制当前的ip
         */
        if (requestCounts > continueCounts) {
            // 限制ip访问的时间[limitTimes]
            redis.set(ipRedisLimitKey, ipRedisLimitKey, limitTimes);
            // 终止请求，返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        return chain.filter(exchange);
    }

    public Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum statusEnum) {
        ServerHttpResponse response = exchange.getResponse();
        GraceJSONResult jsonResult = GraceJSONResult.exception(statusEnum);

        if (!response.getHeaders().containsKey("Content-Type")) {
            response.getHeaders().add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        }

        // 修改response的状态码为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        String resultJson = new Gson().toJson(jsonResult);
        DataBuffer buffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 过滤器的顺序,数字越小优先级越大
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}

