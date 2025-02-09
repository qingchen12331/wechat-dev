package com.qingchen.controller.interceptor;

import com.qingchen.exceptions.MyCustomException;
import com.qingchen.grace.result.ResponseStatusEnum;
import com.qingchen.utils.IPUtil;
import com.qingchen.utils.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static com.qingchen.base.BaseInfoProperties.MOBILE_SMSCODE;


@Slf4j
public class SMSInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redis;
    /**
     * 拦截请求,在controller之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户的ip
        String userIp= IPUtil.getRequestIp(request);
        //获得用于判断是否存在的boolean
        boolean isExit=redis.keyIsExist(MOBILE_SMSCODE+":"+userIp);
        if(isExit){
            log.error("短信发送频率太高了~!!!!");
            throw new MyCustomException(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
        }

        /**
         * false:请求被拦截
         * true:请求放行,正常通过,验证通过
         */
        return true;
    }

    /**
     * 请求controller之后,渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 请求controller之后,渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
