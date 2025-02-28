package com.qingchen.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.service.FriendShipService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("friendship")
@Slf4j
public class FriendShipController extends BaseInfoProperties {
    @Autowired
    private FriendShipService friendShipService;
    @PostMapping("getFriendship")
    public GraceJSONResult getFriendship(String friendId, HttpServletRequest request){
        String myId=request.getHeader(HEADER_USER_ID);
        return GraceJSONResult.ok(friendShipService.getFriendShip(myId,friendId));

    }
    @PostMapping("queryMyFriends")
    public GraceJSONResult queryMyFriends(HttpServletRequest request){
        String myId=request.getHeader(HEADER_USER_ID);
        return GraceJSONResult.ok(friendShipService.queryMyFriends(myId));
    }
    @PostMapping("updateFriendRemark")
    public GraceJSONResult updateFriendRemark(String friendId,String friendRemark,HttpServletRequest request){
        if(StringUtils.isBlank(friendId)||StringUtils.isBlank(friendRemark)){
            return GraceJSONResult.error();
        }
        String myId=request.getHeader(HEADER_USER_ID);
        friendShipService.updateFriendRemark(myId,friendId,friendRemark);
        return GraceJSONResult.ok();
    }


}
