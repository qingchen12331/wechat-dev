package com.qingchen.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.service.FriendCircleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.qingchen.base.BaseInfoProperties.HEADER_USER_ID;

@RestController
@RequestMapping("friendCircle")
public class FriendCircleController {
    @Autowired
    private FriendCircleService friendCircleService;
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody FriendCircleBO friendCircleBO, HttpServletRequest request){
        String userId=request.getHeader(HEADER_USER_ID);
        friendCircleBO.setUserId(userId);
        friendCircleBO.setPublishTime(LocalDateTime.now());
        friendCircleService.publish(friendCircleBO);

        return GraceJSONResult.ok();
    }
    @PostMapping("queryList")
    public GraceJSONResult queryList(String userId, @RequestParam(defaultValue = "1",name = "page")Integer page,
                                     @RequestParam(defaultValue = "10",name = "pageSize")Integer pageSize){
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.error();
        }
        return GraceJSONResult.ok(friendCircleService.queryList(userId,page,pageSize));
    }
}
